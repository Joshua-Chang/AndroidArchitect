import React, {Component} from 'react';
import {Text, View, Button} from 'react-native';
import HiRNBridge from './lib/HiRNBridge'
import HiRNImageView from '../js/lib/HiRNImageView'

class RNBridgeDemo extends Component {
    constructor(props) {
        super(props);
        this.state = {
            header: '',
            message:''
        }
    }

    async getHeader() {
        let header = await HiRNBridge.getHeaderParams();
        this.setState({
            header
        })
    }

    render() {
        const {header,message} = this.state;
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
                <HiRNImageView style={{width: 200, height: 200}}
                               src={'https://www.devio.org/img/beauty_camera/beauty_camera1.jpg'}
                               onPress={(e) => {
                                   this.setState(
                                       {
                                           message: e
                                       }
                                   )
                               }}
                />
                <Text>收到：{message}</Text>
            </View>
        );
    }
}

export default RNBridgeDemo;
