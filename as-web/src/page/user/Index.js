import React from 'react';
import './Index.less'
import {Table, Switch, Popconfirm} from "antd";
import api from "../../service";

const PAGE_SIZE = 5
export default class Index extends React.Component {
    state = {
        data: [],
        total: 0,
        loading: false
    }

    constructor(props) {
        super(props);
        this.columns = [
            {title: 'uid', dataIndex: 'uid'},
            {title: 'imoocId', dataIndex: 'imoocId'},
            {title: '用户名', dataIndex: 'userName'},
            {title: '创建时间', dataIndex: 'createTime'},
            {
                title: 'forbid', dataIndex: 'forbid', width: '20%',
                render: (text, record) => {
                    return <Popconfirm
                        title={`确定要${record.forbid === '1' ? '解禁' : '冻结'}?`}
                        onConfirm={() => this.toggleForbid(record)}
                    >
                        <Switch
                            checkedChildren="正常"
                            unCheckedChildren="冻结"
                            checked={text !== "1"}/>
                    </Popconfirm>
                }
            },
        ]
    }

    render() {
        const {data, total, loading} = this.state;
        return (
            <Table
                loading={loading}
                rowKey={item => item.uid}
                dataSource={data}
                pagination={
                    {
                        total,
                        pageSize: PAGE_SIZE,
                        onChange: (page, pageSize) => {
                            this.loadData(pageSize)
                        }
                    }
                }
                columns={this.columns}
            />
        )
    }

    /*组件挂载时loading，并加载第一页数据*/
    componentDidMount() {
        this.setState({loading: true});
        this.loadData(1);
    }

    loadData = (pageIndex) => {
        this.pageIndex = pageIndex
        api.userList({pageIndex, PAGE_SIZE})
            .then(res => res.json())
            .then(result => {
                /*从result中解构出来*/
                const {code, message, data: {list, total} = {}/*list,total为空时防止报错*/} = result;
                this.setState({
                    loading: false,
                    data: list,
                    total: total
                });
            }).catch(e => {
            this.setState({
                loading: false
            })
            console.log(e)
        })
    }
    toggleForbid = (record) => {
        const forbid = record.forbid === '1' ? 0 : 1;
        /*forbid param uid path*/
        api.updateUser({forbid})(record.uid)
            .then(res => res.json())
            .then(result => {
                this.loadData(this.pageIndex)
            }).catch(e => {
            console.log(e);
        });
    }

}