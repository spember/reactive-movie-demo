var path = require('path')
var webpack = require('webpack')


module.exports = {
    watch: true,
    devtool: 'source-map',
    entry: "./js/index.js",
    output: {
        path: path.join(__dirname, '../ratpack/public/js/dist'),
        filename: 'bundle.js',
        publicPath: 'http://localhost:8080/',
        crossOriginLoading: "anonymous"
    },
    // plugins: [
    //     new webpack.optimize.OccurenceOrderPlugin(),
    //     new webpack.HotModuleReplacementPlugin(),
    //     new webpack.NoErrorsPlugin()
    // ],
    module: {
        loaders: [
            {
                test: /\.scss$/,
                loaders: ["style", "css", "sass"]
            },
            {
                test: /\.js?$/,
                exclude: /(node_modules|bower_components)/,
                loader: ['babel'], // 'babel-loader' is also a legal name to reference
                query: {
                    presets: ['es2015']
                }
            },
            //{ test: /\.js?$/, loaders: ['react-hot'], include: path.join(__dirname, 'js') },
            //{
            //    test: /\.js?$/,
            //    exclude: /(node_modules|bower_components)/,
            //    loader: ['babel'], // 'babel-loader' is also a legal name to reference
            //    query: {
            //        presets: ['react', 'es2015']
            //    }
            //},
            //{test: /\.js$/, loader: "eslint-loader", exclude: /node_modules/},

        ]
    }
}