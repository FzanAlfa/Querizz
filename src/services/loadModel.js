const tf = require('@tensorflow/tfjs-node');
const path = require('path');
require('dotenv').config();

async function loadModel1() {
    const modelPath = path.resolve(__dirname, process.env.MODEL_URL1.replace('file://', ''));

    try {
        // Memuat model menggunakan loadLayersModel
        const model = await tf.loadLayersModel(`file://${modelPath}`);
        console.log("Model berhasil dimuat.");
        return model;
    } catch (error) {
        console.error("Kesalahan saat memuat model:", error);
    }
}


async function loadModel2() {
    return tf.loadGraphModel(process.env.MODEL_URL2);
}

module.exports = {
    loadModel1,
    loadModel2
};
