const { Firestore } = require('@google-cloud/firestore');

// Path to the service account key file
const serviceAccountPath = './kunci/kunci.json';

// Custom Firestore project ID
const customProjectId = 'web-service-capstone'; // Ganti dengan Firestore project ID Anda
const customDatabaseId = 'nosqldatabase'; // Ganti dengan database ID Anda, jika berbeda dari default


const db = new Firestore({
  projectId: customProjectId, // Your custom Firestore project ID
  keyFilename: serviceAccountPath, // Path to your service account key file
  databaseId: customDatabaseId // Database ID, default is "(default)"
});

async function registerUsers(email, data) {
  const usersCollection = db.collection("users");
  
  try {
    // Cek apakah email sudah terdaftar
    const userDoc = await usersCollection.doc(email).get();
    if (userDoc.exists) {
      throw new Error('Email sudah terdaftar, gunakan email lain');
    }

    // Jika email belum terdaftar, simpan data
    await usersCollection.doc(email).set(data);
  } catch (error) {
    // Jika terjadi error, kembalikan response error
    console.error('Error saat menyimpan data:', error);
    throw new Error(error.message);
  }
}

async function getUserByEmail(email) {
  const usersCollection = db.collection("users");

  try {
    // Cari pengguna berdasarkan email
    const userDoc = await usersCollection.doc(email).get();
    if (!userDoc.exists) {
      throw new Error('Email tidak terdaftar');
    }

    // Ambil data pengguna
    const userData = userDoc.data();
    return userData;
  } catch (error) {
    console.error('Error saat mencari pengguna:', error);
    throw new Error(error.message);
  }
}

async function storeData(id, data, email) {
  const predictCollection = db.collection(email); // Mengakses koleksi dengan nama dinamis.
  return predictCollection.doc(id).set(data); // Menyimpan data ke dokumen dengan ID tertentu.
}

async function getAllData(nameUse) {
  const predictCollection = db.collection(nameUse);

  try {
    const snapshot = await predictCollection.get();
    const data = [];
    snapshot.forEach(doc => {
      data.push(doc.data());
    });
    return data;
  } catch (error) {
    console.error('Error saat mengambil data:', error);
    throw error;
  }
}

module.exports = { storeData, getAllData, registerUsers, getUserByEmail };
