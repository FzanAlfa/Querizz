const tf = require('@tensorflow/tfjs-node');
const path = require('path');
require('dotenv').config();

async function loadModel1() {
    return tf.loadGraphModel(process.env.MODEL_URL2);
}

async function loadModel2() {
    return tf.loadGraphModel(process.env.MODEL_URL2);
}

module.exports = {
    loadModel1,
    loadModel2
};
