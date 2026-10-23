const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read user profile", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
});

test("Unauthenticated user: cannot write workout history", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("workout_history").doc("hist_1").set({
    historyId: "hist_1",
    userId: ALICE_UID,
    workoutId: "pelvic_kegel_1",
    durationMinutes: 20,
    createdAt: new Date(),
  }));
});

test("Authenticated user: can create and read own profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).set({
    userId: ALICE_UID,
    name: "Jahangir Alam",
    language: "bn",
    goal: "Kegel Pelvic Fitness",
    createdAt: new Date(),
  }));

  const doc = await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).get());
});

test("Cross-user isolation: Bob cannot read Alice profile", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      name: "Alice",
      language: "en",
      createdAt: new Date(),
    });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user: can log workout history and query own history", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("workout_history").doc("hist_123").set({
    historyId: "hist_123",
    userId: ALICE_UID,
    workoutId: "pelvic_kegel_1",
    workoutTitleBn: "পেলভিক কেগেল ওয়ার্কআউট",
    workoutTitleEn: "Pelvic Kegel Workout",
    durationMinutes: 15,
    exercisesCompleted: 10,
    totalExercises: 10,
    caloriesBurned: 85,
    createdAt: new Date(),
  }));

  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("workout_history").get());
});

test("Cross-user isolation: Bob cannot read Alice workout history", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(ALICE_UID).collection("workout_history").doc("hist_alice").set({
      historyId: "hist_alice",
      userId: ALICE_UID,
      workoutId: "pelvic_kegel_1",
      durationMinutes: 20,
      createdAt: new Date(),
    });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("workout_history").doc("hist_alice").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("workout_history").get());
});

test("Authenticated user: can save and read favorite exercises", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("favorites").doc("ex_kegel_1").set({
    exerciseId: "ex_kegel_1",
    userId: ALICE_UID,
    titleBn: "বেসিক পেলভিক ব্রিজিং কেগেল",
    titleEn: "Basic Pelvic Bridge Kegel",
    createdAt: new Date(),
  }));

  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).collection("favorites").get());
});
