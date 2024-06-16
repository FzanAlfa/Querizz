const tf = require('@tensorflow/tfjs-node');
const fs = require('fs').promises; 
const PDFParser = require('pdf-parse');
const InputError = require('../exceptions/InputError');

//async function predictFile(model, bufferdata) {
//  try {
//    // Pastikan data yang diberikan adalah dalam bentuk Buffer
//    if (!Buffer.isBuffer(bufferdata)) {
//      throw new Error('Input harus berupa buffer data');
//    }
//
//    // Menggunakan pdf-parse untuk mengonversi PDF ke teks
//    const data = await PDFParser(bufferdata);
//    const summarize = data.text;
//    return summarize;
//  } catch (error) {
//    console.error('Error saat memproses file:', error);
//    throw error; // atau lakukan penanganan error yang sesuai
//  }
//}
 
async function predictFile(model, bufferdata) {
  try {
    // Pastikan data yang diberikan adalah dalam bentuk Buffer
    if (!Buffer.isBuffer(bufferdata)) {
      throw new Error('Input harus berupa buffer data');
    }

    // Konversi buffer ke string
    const textData = bufferdata.toString('utf8');

    // Lakukan preprocessing pada teks jika diperlukan
    // Misalnya, tokenisasi dan padding
    const processedData = preprocessText(textData);

    // Membuat tensor dari data yang telah diproses
    const tensor = tf.tensor2d(processedData);

    // Menggunakan tensor sebagai input untuk model
    const prediction = model.predict(tensor);

    return prediction;
  } catch (error) {
    console.error('Error saat memproses file:', error);
    throw error; // atau lakukan penanganan error yang sesuai
  }
}

function preprocessText(text) {
  // Contoh sederhana preprocessing: tokenisasi menjadi kata dan konversi ke indeks angka
  const words = text.split(' ');
  const maxLength = 100; // Panjang maksimal sequence
  const vocab = {'this': 1, 'is': 2, 'an': 3, 'example': 4}; // Contoh kamus kata ke indeks
  const data = words.map(word => vocab[word.toLowerCase()] || 0);

  // Padding atau trimming
  if (data.length < maxLength) {
    data.push(...Array(maxLength - data.length).fill(0)); // Padding dengan 0
  } else {
    data.length = maxLength; // Trimming jika lebih panjang dari maxLength
  }

  return [data]; // Kembalikan dalam bentuk array 2D (batch_size, sequence_length)
}


module.exports = {
  predictFile,
}
