const predictClassification = require('../services/inferenceService');
const crypto = require('crypto');
const {storeData, getAllData, registerUsers, getUserByEmail} = require('../services/storeData');
const Joi = require('@hapi/joi');
const jwt = require('jsonwebtoken');
//const secretKey = process.env.secret_Key;

async function registUsers(request, h) {
  try {
    const { name, email, password } = request.payload;

    // Validasi name harus minimal 4 karakter
    if (name.length < 4) {
      return h.response({
        status: 'fail',
        message: 'Nama harus minimal 4 karakter'
      }).code(400); // Bad Request
    }

    // Validasi email harus berformat email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      return h.response({
        status: 'fail',
        message: 'Email tidak valid'
      }).code(400); // Bad Request
    }

    // Validasi password harus minimal 8 karakter
    if (password.length < 8) {
      return h.response({
        status: 'fail',
        message: 'Password harus minimal 8 karakter'
      }).code(400); // Bad Request
    }

    const data = {
      "nama": name,
      "email": email,
      "password": password
    };

    try {
      // Panggil fungsi registerUsers dengan data yang telah divalidasi
      await registerUsers(email, data);
    } catch (error) {
      // Jika terjadi error saat memanggil registerUsers, tangkap error tersebut dan kembalikan response error
      return h.response({
        status: 'fail',
        message: `Gagal mendaftar: ${error.message}`
      }).code(500); // Internal Server Error
    }

    // Kembalikan response sukses setelah pendaftaran
    const response = h.response({
      status: 'success',
      message: 'Berhasil mendaftar!',
    });
    response.code(200);
    return response;
  } catch (error) {
    // Jika terjadi error lain di luar registerUsers, kembalikan response error
    return h.response({
      status: 'fail',
      message: error.message
    }).code(500); // Internal Server Error
  }
}



async function loginUser(request, h) {
  try {
    const { email, password } = request.payload;

    // Validasi email harus berformat email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      return h.response({
        status: 'fail',
        message: 'Email tidak valid'
      }).code(400); // Bad Request
    }

    // Validasi password harus minimal 8 karakter
    if (password.length < 8) {
      return h.response({
        status: 'fail',
        message: 'Password harus minimal 8 karakter'
      }).code(400); // Bad Request
    }

    try {
      // Panggil fungsi getUserByEmail untuk mendapatkan data pengguna
      const userData = await getUserByEmail(email, password);

      // Kembalikan response sukses jika login berhasil
      const response = h.response({
        status: 'success',
        message: 'Berhasil login!',
        userData
      });
      response.code(200);
      return response;
    } catch (error) {
      // Jika terjadi error saat mencari pengguna atau lainnya, kembalikan response error
      return h.response({
        status: 'fail',
        message: `Gagal login: ${error.message}`
      }).code(500); // Internal Server Error
    }
  } catch (error) {
    // Jika terjadi error lain di luar pencarian pengguna, kembalikan response error
    return h.response({
      status: 'fail',
      message: `Gagal error lain: ${error.message}`
    }).code(500); // Internal Server Error
  }
}


async function postPredictHandler(request, h) {
  const { image } = request.payload;
  const { model } = request.server.app;
 
  const { label, suggestion } = await predictClassification(model, image);
  const name = nameUse;
  const id = crypto.randomUUID();
  const createdAt = new Date().toISOString();
 
  const data = {
    "id": id,
    "result": label,
    "suggestion": suggestion,
    "createdAt": createdAt
  }

  await storeData(id, data, name);
 
  const response = h.response({
    status: 'success',
    message: 'Model is predicted successfully',
    data
  })
  response.code(201);
  return response;
}



async function allHistories(request, h) {
  try {
    const allData = await getAllData(email);
    const response = h.response({
      status: 'success',
      data: allData,
    })
    response.code(200);
    return response;
  } catch (error) {
    console.error('Error:', error);
    return h.response({ message: 'Terjadi kesalahan saat mengambil data.' }).code(500);
  }
}



module.exports = {postPredictHandler, allHistories, registUsers, loginUser};
