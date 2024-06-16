const bcrypt = require('bcrypt');
const Joi = require('@hapi/joi');
const jwt = require('jsonwebtoken');
const { format } = require('date-fns');
const { storeData, getAllData, registerUsers, getUserByEmail } = require('../../services/storeData');
const { predictFile} = require('../../services/inferenceService');
const { generateId } = require('../../../random/generateRandom'); 
const pdf = require('pdf-parse');

const userSchema = Joi.object({
  name: Joi.string().min(4).required().messages({
    'string.min': 'Nama harus minimal 4 karakter',
    'any.required': 'Nama harus diisi',
  }),
  email: Joi.string().email().required().messages({
    'string.email': 'Email tidak valid',
    'any.required': 'Email harus diisi',
  }),
  password: Joi.string().min(8).required().messages({
    'string.min': 'Password harus minimal 8 karakter',
    'any.required': 'Password harus diisi',
  }),
});

async function registUsers(request, h) {
  try {
    const { name, email, password } = request.payload;

    // Validasi payload dengan Joi
    const { error } = userSchema.validate({ name, email, password });
    if (error) {
      return h.response({
        status: 'fail',
        message: error.details[0].message,
      }).code(400); // Bad Request
    }

    // Enkripsi password menggunakan bcrypt
    const hashedPassword = await bcrypt.hash(password, 10);

    const data = { nama: name, email, password: hashedPassword };

    try {
      // Panggil fungsi registerUsers dengan data yang telah divalidasi
      await registerUsers(email, data);
    } catch (error) {
      return h.response({
        status: 'fail',
        message: `Gagal mendaftar: ${error.message}`,
      }).code(500); // Internal Server Error
    }

    // Kembalikan response sukses setelah pendaftaran
    return h.response({
      status: 'success',
      message: 'Berhasil mendaftar!',
    }).code(200);
  } catch (error) {
    return h.response({
      status: 'fail',
      message: error.message,
    }).code(500); // Internal Server Error
  }
}

async function loginUser(request, h) {
  try {
    const { email, password } = request.payload;
    const userData = await getUserByEmail(email);

    if (!userData) {
      return h.response({
        status: 'fail',
        message: 'Email atau password salah',
      }).code(401); // Unauthorized
    }

    // Verifikasi password menggunakan bcrypt
    const isPasswordValid = await bcrypt.compare(password, userData.password);
    if (!isPasswordValid) {
      return h.response({
        status: 'fail',
        message: 'Email atau password salah',
      }).code(401); // Unauthorized
    }

    const token = jwt.sign({ email: userData.email }, process.env.JWT_SECRET, { expiresIn: '1h' });

    return h.response({
      status: 'success',
      message: 'Berhasil login!',
      nama: userData.nama,
      token,
    }).code(200);
  } catch (error) {
    return h.response({
      status: 'fail',
      message: `Gagal login: ${error.message}`,
    }).code(500);
  }
}


async function postPredictHandler(request, h) {
  try {
    const { file, title, subtitle } = request.payload;
    const { models } = request.server.app;
    const user = request.auth.credentials.user;

    const userData = await getUserByEmail(user.email);

    // Perform predictions with both models
    const summarize = await predictFile(models.model1, file);
    const id = generateId();
    const createdAt = new Date(); // current timestamp

    const formattedDate = format(createdAt, "yyyy-MM-dd HH.mm"); // format time 

    const data = {
      username: userData.nama,
      id,
      title,
      subtitle,
      results: [
        {
          modellll: 'ini summerize',
          text: summarize,
        },
      ],
      createdAt: formattedDate,
    };
    
    //await storeData(id, data, user.email);

    return h.response({
      status: 'success',
      message: 'model berhasil diprediksi',
      data,
    }).code(201);
  } catch (error) {
    return h.response({
      status: 'fail',
      message: `Gagal melakukan prediksi: ${error.message}`,
    }).code(500);
  }
}


async function allHistories(request, h) {
  try {
    const user = request.auth.credentials.user;
    const allData = await getAllData(user.email);

    return h.response({
      status: 'success',
      data: allData,
    }).code(200);
  } catch (error) {
    console.error('Error:', error);
    return h.response({
      status: 'fail',
      message: 'Terjadi kesalahan saat mengambil data.',
    }).code(500);
  }
}

module.exports = { postPredictHandler, allHistories, registUsers, loginUser };
