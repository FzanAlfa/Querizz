const { options } = require('@hapi/joi');
const {postPredictHandler, allHistories, registUsers, loginUser} = require('./handler/handler');
 
const routes = [
  {
    path: '/register',
    method: 'POST',
    options: {
      auth: false,
      handler: registUsers,
    }
  },
  {
    method: 'POST',
    path: '/login',
    options: {
        auth: false, // Tidak memerlukan autentikasi
        handler: loginUser,
    },
},
{
    method: 'POST',
    path: '/upload',
    options: {
        auth: 'jwt', // Memerlukan autentikasi
        payload: {
            allow: 'multipart/form-data',
            maxBytes: 10000000,
            multipart: true,
        },
        handler: postPredictHandler,
    },
},
{
  path: '/histories',
  method: 'GET',
  options: {
    auth: 'jwt',
    handler: allHistories,
},
},
//{
//  method: 'GET',
//  path: '/{param*}',
//  options: {
//    auth: false,
//    handler: (request, h) => {
//      return 'backend berhasil!';
//    }
//  }
//}

]
 
module.exports = routes;
