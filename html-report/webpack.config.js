const path = require('path');
const HtmlWebPackPlugin = require("html-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const CopyWebpackPlugin = require('copy-webpack-plugin');

const SOURCE_DIR = path.resolve(__dirname, 'src');
const JAVASCRIPT_DIR = path.resolve(__dirname, 'src/js');

module.exports = {
    context: SOURCE_DIR,
    entry: {
        app: './js/index.js'
    },
    resolve: {
        modules: [
            path.resolve(JAVASCRIPT_DIR),
            path.resolve(__dirname, 'node_modules')
        ]
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: [
                    { loader: "babel-loader" },
                    { loader: "eslint-loader" }
                ]
            },
            {
                test: /\.css$/,
                use: [MiniCssExtractPlugin.loader, "css-loader"]
            }
        ]
    },
    plugins: [
        new HtmlWebPackPlugin({
            template: "index.html",
        }),
        new MiniCssExtractPlugin({
            filename: "[name].css",
            chunkFilename: "[id].css"
        }),
        new CopyWebpackPlugin([
            {
                from: 'provided.js',
            },
        ])
    ]
};