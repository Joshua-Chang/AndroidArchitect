/**
 * @format
 */

import {AppRegistry} from 'react-native';
import {name as appName} from './app.json';
import BrowsingPage from "./js/page/BrowsingPage";
import RNBridgeDemo from "./js/RNBridgeDemo";
import React, {Component} from 'react';

const PageRoute = {
    '/browsing': BrowsingPage,
    '/bridgeDemo': RNBridgeDemo
};

class Index extends Component {
    getPageComponent() {
        const {routeTo} = this.props;
        const page = PageRoute[routeTo]
        return page
    }

    render() {
        const Page = this.getPageComponent();
        return <Page/>
    }
}

//单RN实例的方式
// AppRegistry.registerComponent(appName, () => Index);

//多RN实例的方式
AppRegistry.registerComponent('rn_module_browsing', () => BrowsingPage);
AppRegistry.registerComponent('rn_module_bridgeDemo', () => RNBridgeDemo);
