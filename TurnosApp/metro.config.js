const { getDefaultConfig } = require("expo/metro-config");
const { withNativeWind } = require('nativewind/metro');
const config = getDefaultConfig(__dirname)

// Por defecto viene ./global.css pero el nuestro esta dentro de app
module.exports = withNativeWind(config, { input: './app/global.css' })