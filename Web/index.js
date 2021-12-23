require("dotenv").config();

let C, express;
const path = require('path');
// prettier-ignore
try {
    C = require("chalk");
    express = require("express");
} catch(e) {
    return console.log(e.message.split('\n')[0] + ". Are you sure you did 'npm i' before the installation?")
}

const app = express();
const PORT = process.env.PORT || 8080;

// Logging Utilities
const l = console.log;
const info = (s) => l(C.yellow(C.bold("[INFO]>") + " " + s));
const error = (s, severe = false) =>
    l(C.red(C.bold("[ERROR]>" + (severe ? " (severe)" : "")) + " " + s));
const success = (s) => l(C.green(C.bold("[SUCCESS]>") + " " + s));

app.use('/static', express.static('public'))

app.get("/ad/:uuid", (req, res) => {
    res.sendFile(path.join(__dirname, '/public/ad.html'));
});


app.listen(PORT, () => {
    success("Successfully started AdWatcher.");
    info("This application is currently running on " + C.bold(`port ${PORT}.`));
});
