import React, {Component} from 'react';
import {Text, View, Button} from 'react-native';
import HiRNBridge from './lib/HiRNBridge'

class RNBridgeDemo extends Component {
    constructor(props) {
        super(props);
        this.state = {
            header: ''
        }
    }

    async getHeader() {
        let headers = await HiRNBridge.getHeaderParams();
        this.setState({
            header
        })
    }

    render() {
        const {header} = this.state;
        return (
            <View>
                <Button title={'onBack'} onPress={() => {
                    HiRNBridge.onBack(null)/*调用back*/
                }}/>
                <Button title={'goToNative'} onPress={() => {
                    HiRNBridge.goToNative({'goodsId': '1580374361011'})
                }}/>
                <Button title={'getHeaderParams'} onPress={() => {
                    this.getHeader();
                }}/>
                <Text>获取的:{JSON.stringify(header)}</Text>
            </View>
        );
    }
}

export default RNBridgeDemo;
