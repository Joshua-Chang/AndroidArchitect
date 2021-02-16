import React, { Component } from 'react';
import { View, StyleSheet, Text, TouchableOpacity, Image, StatusBar, Animated } from 'react-native';

const NAV_BAR_HEIGHT = 50;
const STATUS_BAR_HEIGHT = 20;
export default class NavigationBar extends Component {
    componentDidMount(){
        StatusBar.setBarStyle('dark-content',true)
        StatusBar.setTranslucent(true)
        StatusBar.setBackgroundColor('transparent')
    }
    render() {
        let titleView = <Text ellipsizeMode='head' numberOfLines={1} style={styles.title}> {this.props.title}</Text >;
        let content = this.props.hide ? null : <View style={styles.navBar}>
            {this.getBackButton()}
            <View style={styles.navBarTitleContainer}>{titleView}</View>
        </View>;
        return <View style={[styles.container, this.props.style]}>
            {content}
            <View style={styles.line} />
        </View>;
    }
    getBackButton() {
        return <TouchableOpacity onPress={() => {
            this.props.onBack && this.props.onBack/* 设置了就回调 */
        }}>
            <View style={styles.backIconContainer}>
                <Image style={styles.backIcon}
                    source={require('../../res/image/back.png')}
                >
                </Image>
            </View>
        </TouchableOpacity>
    }
}
const styles = StyleSheet.create({
    container: {
        backgroundColor: 'white',
        paddingTop: STATUS_BAR_HEIGHT
    },
    navBar: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        height: NAV_BAR_HEIGHT,
    },
    line: {
        height: 1,
        backgroundColor: '#d2d2d2',
    },
    navBarTitleContainer: {
        alignItems: 'center',
        justifyContent: 'center',
        position: 'absolute',
        left: 40,
        right: 40,
        top: 0,
        bottom: 0,
    },
    backIconContainer: {
        padding: 6
    },
    backIcon: {
        height: 20,
        width: 20
    },
    title:{
        fontSize:15,
        color:'#151515'
    }
});
