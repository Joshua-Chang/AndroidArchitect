import React from 'react';
import './Index.less'
import {Button} from "antd";
import api from "../service";

export default class Index extends React.Component {
    state={
        result:{}
    }
    fire = () => {
        api.userList({pageIndex: 1, pageSize: 10})
            .then(res => res.json())
            .then(result => {
                this.setState({result});
            }).catch(e=>{console.log(e);})
    };

    render() {
        const {result}=this.state;
        return <div className='home'>
            <Button onClick={this.fire/*加括号会默认调一次*/}>test Api</Button>
            <div>
                Result:{JSON.stringify(result)}
            </div>
        </div>
    }
}