/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import BrowsingPage from './js/page/BrowsingPage';

AppRegistry.registerComponent(appName, () => BrowsingPage);
