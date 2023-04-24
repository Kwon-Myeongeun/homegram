const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp(functions.config().firebase);

var database = admin.database();

var dailyRef = database.ref("daily/");
var groupRef = database.ref("group/");
var todoRef = database.ref("todo/");
var locationRef = database.ref("location/");

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
    
exports.cleanDailyScheduledFunction = functions.pubsub.schedule("0 6 * * *")
    .timeZone("Asia/Seoul")
    .onRun(async (context) => {
        dailyRef.once("value").then(dailySnapshot => {
            dailySnapshot.forEach(group => {
                groupRef.child(group.key).once("value").then(groupSnapshot => {
                    if (!groupSnapshot.exists()) {
                        dailyRef.child(group.key).remove();
                    }
                });
            });
        });
        return null;
    });
    
exports.cleanTodoScheduledFunction = functions.pubsub.schedule("0 7 * * *")
    .timeZone("Asia/Seoul")
    .onRun(async (context) => {
        todoRef.once("value").then(todoSnapshot => {
            todoSnapshot.forEach(group => {
                groupRef.child(group.key).once("value").then(groupSnapshot => {
                    if (!groupSnapshot.exists()) {
                        todoRef.child(group.key).remove();
                    }
                });
            });
        });
        return null;
    });
    
exports.cleanLocationScheduledFunction = functions.pubsub.schedule("0 8 * * *")
    .timeZone("Asia/Seoul")
    .onRun(async (context) => {
        locationRef.once("value").then(locationSnapshot => {
            locationSnapshot.forEach(group => {
                groupRef.child(group.key).once("value").then(groupSnapshot => {
                    if (!groupSnapshot.exists()) {
                        locationRef.child(group.key).remove();
                    }
                });
            });
        });
        return null;
    });