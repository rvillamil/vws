// server.js
const jsonServer = require('json-server')
const server = jsonServer.create()
const router = jsonServer.router('shows.json')
const middlewares = jsonServer.defaults()

// Set default middlewares (logger, static, cors and no-cache)
server.use(middlewares)

// Add this before server.use(router)
server.use(jsonServer.rewriter({
    '/api/*': '/$1',
}))

// To handle POST, PUT and PATCH you need to use a body-parser
// You can use the one used by JSON Server
server.use(jsonServer.bodyParser)
server.use((req, res, next) => {
    if (req.path === '/login') {
        console.log('Login fake: HTTP 200OK');
        res.sendStatus(200);
    }
    // Continue to JSON Server router
    next();
})

// Use default router
server.use(router)
server.listen(8080, () => {
    console.log('JSON Server is running on port 8080!!')
})