import React from 'react';
import {Layout, Menu} from 'antd';
import {withRouter} from 'react-router-dom';
import './DrawerMenu.less'
import {
    AppstoreAddOutlined,
    HomeOutlined,
    PicCenterOutlined,
    ProjectOutlined,
    TeamOutlined,
    ShopOutlined,
    FileAddFilled
} from "@ant-design/icons";

const {Sider} = Layout;
const {SubMenu} = Menu;
const MENUS = {
    home: {
        key: 'home',
        title: '首页'
    }, user: {
        key: 'user',
        title: '用户管理'
    }, category: {
        key: 'category',
        title: '类别列表'
    }, addCategory: {
        key: 'addCategory',
        title: '添加类别'
    }, configList: {
        key: 'configList',
        title: '配置列表'
    }, configAdd: {
        key: 'configAdd',
        title: '添加配置'
    },
};

class Index extends React.Component {
    state = {
        selectedKeys: ['home']
    };
    onCollapse = collapsed => {
        this.setState({collapsed})
    };
    onSelect = selectedKeys => {
        this.setState({
            selectedKeys: [selectedKeys.key]
        });
        let pathName;
        switch (selectedKeys.key) {
            case 'home':
                pathName = "/";
                break;
            case 'category':
                pathName = "/category";
                break;
            case 'addCategory':
                pathName = "/category-add";
                break;
            case 'user':
                pathName = "/user";
                break;
            case 'configList':
                pathName = "/config";
                break;
            case 'configAdd':
                pathName = "/config-add";
                break;
            default:
                break
        }
        const {history, onMenuSelect} = this.props;
        const menu=MENUS[selectedKeys.key];
        if (pathName){
            history.push(pathName);
            /*onMenuSelect不为空时调用此方法*/
            onMenuSelect&&onMenuSelect(pathName,menu.title);
        }
    };

    menu() {
        return <Menu theme='dark' defaultSelectedKeys={this.state.selectedKeys} mode='inline'
                     onSelect={this.onSelect}>
            <Menu.Item key={MENUS.home.key} icon={<HomeOutlined/>}>
                {MENUS.home.title}
            </Menu.Item>
            <SubMenu key='category' title='类别管理' icon={<ProjectOutlined/>}>
                <Menu.Item key={MENUS.category.key} icon={<AppstoreAddOutlined/>}>
                    {MENUS.category.title}
                </Menu.Item>
                <Menu.Item key={MENUS.addCategory.key} icon={<PicCenterOutlined/>}>
                    {MENUS.addCategory.title}
                </Menu.Item>
            </SubMenu>
            <Menu.Item key={MENUS.user.key} icon={<TeamOutlined/>}>
                {MENUS.user.title}
            </Menu.Item>
            <SubMenu key='configCenter'
                     title='配置中心'
                     icon={<ProjectOutlined/>}>
                <Menu.Item key={MENUS.configList.key} icon={<ShopOutlined/>}>
                    {MENUS.configList.title}
                </Menu.Item>
                <Menu.Item key={MENUS.configAdd.key} icon={<FileAddFilled/>}>
                    {MENUS.configAdd.title}
                </Menu.Item>
            </SubMenu>
        </Menu>
    }

    render() {
        const {collapsed} = this.props
        const headerTitle = collapsed ? null :
            <div className='drawer-header-text-container'>
                <label className='drawer-header-text'>架构师</label>
                <label className='drawer-header-text'>后台</label>
            </div>
        return (
            <Sider trigger={null} collapsed={collapsed} collapsible onCollapse={this.onCollapse}>
                <div className='drawer-header'>
                    <img className='drawer-logo' alt="logo" src="https://www.devio.org/img/avatar.png"/>
                    {headerTitle}
                </div>
                {this.menu()}
            </Sider>
        )
    }
}

//withRouter是react-router的一个高阶组件，获取history
//render时会把match，location和history出入props
export default withRouter(Index);