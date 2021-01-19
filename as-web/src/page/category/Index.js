import React from 'react';
import './Index.less'
import {Table, Popconfirm} from "antd";
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
            {title: '商品ID', dataIndex: 'categoryId', width: '20%'},
            {title: '类别名称', dataIndex: 'categoryName'},
            {title: '创建时间', dataIndex: 'createTime'},
            {
                title: '操作', dataIndex: 'operation', width: '20%',
                render: (text, record) => {
                    return <Popconfirm
                        title={`确定要删除${record.categoryName}?`}
                        onConfirm={() => this.removeCategory(record)}
                    >
                        <a>删除</a>
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

    loadData = pageIndex => {
        this.pageIndex = pageIndex
        api.categoryList({pageIndex, PAGE_SIZE}).then(res => res.json())
            .then(result => {
                const {data: {list, total} = {}} = result
                this.setState({
                    loading: false,
                    data: list,
                    total: total
                })
            }).catch(e => {
            this.setState({
                loading: false
            })
            console.log(e)
        })
    }
    removeCategory = record => {
        api.removeCategory(record.categoryId)
            .then(res => res.json())
            .then(result => {
                this.loadData(this.pageIndex)
            }).catch(e => {
            console.log(e);
        });
    }
}