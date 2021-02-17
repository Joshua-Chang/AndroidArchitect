import React, {Component} from 'react';
import {View, StyleSheet, FlatList, RefreshControl, DeviceEventEmitter} from 'react-native';
import DataStore from '../lib/DataStore';
import NavigationBar from '../component/NavigationBar';
import BrowsingItem from '../item/BrowsingItem';
import HiRNBridge from '../lib/HiRNBridge'

const PAGE_SIZE = 10;
const REFRESH_COLOR = "#d44949";
const URL = 'http://10.0.2.2:8080/as/browsing/histories?'

export default class BrowsingPage extends Component {
    constructor(props) {
        super(props);
        this.state = {list: [], isLoading: false};
        this.pageIndex = 1;
    }

    componentDidMount() {/*装载完成后，添加android发来的事件的监听*/
        this.eventListener = DeviceEventEmitter.addListener("HI_RN_EVENT", (params) => {
            /*||代表为空,返回{},&&代表不为空*/
            const {method} = params || {};
            if (method === 'onStart') {/*接收到该事件时*/
                //滚动回，复位
                this.flatList.scrollToOffset({animated: false, offset: 0});
                if (this.state.list.length === 0) {
                    this.loadData()
                }
            }
        });
        this.loadData();
    }

    componentWillUnmount(): void {
        this.eventListener && this.eventListener.remove();/*取消监听*/
    }

    async loadData(loadMore) {
        if (loadMore) {
            this.pageIndex++;
        } else {
            this.pageIndex = 1;
        }
        const url = `${URL}pageIndex=${this.pageIndex}&pageSize=${PAGE_SIZE}`;
        try {
            const response = await new DataStore().fetchData(url)
            const {list, total} = response;
            if (list && list.length > 0) {
                this.setState(
                    {
                        list: loadMore ? this.state.list.concat(list) : list,
                        isLoading: false
                    }
                )
            } else {
                if (loadMore) {
                    this.pageIndex--;
                }
                this.setState({
                    isLoading: false
                })
            }
        } catch (e) {
            console.log(e);
            if (loadMore) {
                this.pageIndex--;
            }
            this.setState({
                isLoading: false
            })
        }
    }

    renderItem(data) {
        const item = data.item;
        return <BrowsingItem model={item} onSelect={() => {

        }}></BrowsingItem>;
    }

    render() {
        const {list, isLoading} = this.state;
        return <View style={styles.container}>
            <NavigationBar
                title={'历史浏览'}
                onBack={() => {
                    HiRNBridge.onBack(null)
                }}
            ></NavigationBar>
            <FlatList
                ref={ref => this.flatList = ref}
                data={list}
                renderItem={data => this.renderItem(data)}
                keyExtractor={item => '' + item.goodsId}/* 唯一性 */
                refreshControl={
                    <RefreshControl title={'Loading'} titleColor={REFRESH_COLOR}
                                    colors={[REFRESH_COLOR, '#d44900', '#d40049']} refreshing={isLoading}
                                    tintColor={'#d44900'} onRefresh={() =>
                        this.loadData()
                    }></RefreshControl>
                }
                onEndReached={() => {/* 到达尾部 */
                    console.log('-----onEdnReached-----');
                    setTimeout(() => {/* 官方bug onEndReached会调两次，通过延时解决 */
                        if (this.canloadMore) {
                            this.loadData(true);
                            this.canloadMore = false;
                        }
                    }, 100);
                }}
                onEndReachedThreshold={0.5}/* 到达尾部的触发距离 */
                onMomentumScrollBegin={() => {
                    this.canloadMore = true;
                    console.log('-----onMomentumScrollBegin-----');
                }}
            ></FlatList>
        </View>;
    }
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
    }
});
