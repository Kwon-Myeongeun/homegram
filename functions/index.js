const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp(functions.config().firebase);

var database = admin.database();

var dailyRef = database.ref("daily/");

exports.dailyContentScheduledFunction = functions.pubsub.schedule("0 9 * * *")
    .timeZone("Asia/Seoul")
    .onRun(async (context) => {
        dailyRef.once("value").then(snapshot => {
            snapshot.forEach(group => {
                var groupRef = database.ref("daily/" + group.key + "/");
                groupRef.orderByChild("no").limitToLast(1).once("value").then(maxGroup => {
                    maxGroup.forEach(function(data) {
                        var isDoneVal = data.child("isDone").val();
                        if(isDoneVal){
                            var nextVal = parseInt(data.child("no").val()) + 1;
                            database.ref("question/" + nextVal + "/").once("value").then(newContents => {
                                groupRef.push().set({ no: nextVal, isDone: false, contents: newContents.val() })
                            });
                        }
                    });
                });
            });
        });
        return null;
    });