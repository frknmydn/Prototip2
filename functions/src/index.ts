import * as functions from "firebase-functions";

 export const helloWorld = functions.https.onRequest((request, response) => {
   functions.logger.info("Hello logs!", {structuredData: true});
   response.send("Merhaba biz deneme amaçlı TypeScript kullanarak bir fonksyion oluşturduk!");
 });
