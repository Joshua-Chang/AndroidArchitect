/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import BrowsingPage from './js/page/BrowsingPage';
import AsyncStorageDemo from './js/palyground/AsyncStorageDemo';

AppRegistry.registerComponent(appName, () => AsyncStorageDemo);
