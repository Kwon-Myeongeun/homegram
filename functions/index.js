const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp(functions.config().firebase);

var database = admin.database();

exports.dailyScheduledFunction = functions.pubsub.schedule("every 1 minutes")
    .timeZone("Asia/Seoul")
    .onRun(async (context) => {
        database.ref("test/").set("testDate");
        return null;
    });