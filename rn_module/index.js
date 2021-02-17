/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import RNBridgeDemo from "./js/RNBridgeDemo";

AppRegistry.registerComponent(appName, () => RNBridgeDemo);
// AppRegistry.registerComponent(appName, () => App);
