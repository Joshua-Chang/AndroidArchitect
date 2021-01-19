/* craco.config.js */
const CracolessPlugin=require('craco-less')
module.exports = {
    plugins:[
        {
            plugin:CracolessPlugin,
            options:{
                lessLoaderOptions:{
                    lessOptions:{
                        modifyVars:{'@primary-color':'#1da57a'},
                        javascriptEnabled:true
                    }
                }
            }
        }
    ]
};