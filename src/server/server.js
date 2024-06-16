require('dotenv').config();

const Hapi = require('@hapi/hapi');
const Jwt = require('@hapi/jwt');
const routes = require('../server/routes');
const { loadModel1, loadModel2 } = require('../services/loadModel');
const InputError = require('../exceptions/InputError');

(async () => {
    const server = Hapi.server({
        port: 3000,
        host: 'localhost',
        routes: {
            cors: {
                origin: ['*'],
            },
        },
    });

    await server.register(Jwt);

    server.auth.strategy('jwt', 'jwt', {
        keys: process.env.JWT_SECRET,
        verify: {
            aud: false,
            iss: false,
            sub: false,
            nbf: true,
            exp: true,
            maxAgeSec: 14400, // 4 hours
        },
        validate: (artifacts, request, h) => {
            return {
                isValid: true,
                credentials: { user: artifacts.decoded.payload },
            };
        },
    });

    server.auth.default('jwt');

    // Load both models
    const model1 = await loadModel1();
    //const model2 = await loadModel2();

    // Attach both models to server.app
    server.app.models = {
        model1,
        //model2
    };

    server.route(routes);

    await server.start();
    console.log(`Server started at: ${server.info.uri}`);
})();
