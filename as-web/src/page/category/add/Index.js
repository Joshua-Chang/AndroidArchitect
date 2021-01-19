import React from 'react';
import {Form, Input, Button, notification} from 'antd';
import './Index.less'
import api from '../../../service';

export default class Index extends React.Component {
    formRef = React.createRef();/*formRef表单失去焦点/获取焦点的操作*/
    render() {
        return <Form
            className='category-add'
            ref={this.formRef}
            name='control-ref'
            onFinish={this.onFinish}
        >
            <Form.Item name='categoryName' label='类别名' extra='通过|分割来一次性添加多条记录'
                       rules={[{required: true}]}>
                <Input/>
            </Form.Item>
            <Form.Item className='category-add-btn-layout'>
                <Button className='category-add-btn' type='primary' htmlType='submit'>提交</Button>
                <Button htmlType='button' onClick={this.onRest}>重置</Button>
            </Form.Item>


        </Form>
    }

    onFinish = ({categoryName})/*传递过来的values内部就是categoryName，此处可以解构*/ => {
        api.addCategory()({categoryName})/*第二个参数作为查询参数*/
        const nameArray = categoryName.replace(' ', '').split('|');
        const promises = [];
        nameArray.forEach(value => {
            promises.push(this.addCategory(value))
        })
        /*Promise.allSettled数组一次执行,并带有成败后的结果*/
        /*Promise.all数组一次执行,（失败）reject则中断*/
        Promise.allSettled(promises).then(results => {
            /*成败的数组*/
            const successArray = [];
            const failArray = [];
            results.forEach(result => {
                const {status, value, reason} = result;
                if (status === 'fulfilled') {
                    successArray.push(value.categoryName);
                } else /*reject*/{
                    {
                        failArray.push(reason);
                    }
                }
            });
            /*分别处理成败的数组*/
            this.showFailResult(failArray);
            this.showSuccessResult(successArray);
        })
    };

    addCategory(categoryName) {
        return new Promise(/*Promise执行成败两种状态*/(resolve, reject) => {
            /*第一个参数作为body参数，第二个参数作为URL path或者查询参数*/
            // api.addCategory()(categoryName)第二个参数作为URL path
            api.addCategory()({categoryName})/*第二个参数作为查询参数*/
                .then(res => res.json())
                .then(result => {
                    const {code, message} = result;
                    if (code === 0) {
                        resolve({categoryName, message});
                    } else {
                        reject({categoryName, message});
                    }
                }).catch(e => {/*失败的原因*/
                reject({categoryName, message: e.toString()});
            })
        })
    }
    onRest = () => {
        this.formRef.current.resetFields();
    };

    showSuccessResult(successArray) {
        if (!successArray || successArray.length === 0) {
            return;
        }
        notification['success']({
            placement: 'bottomRight',
            message: '添加成功',
            description: successArray.toString()
        })
    }

    showFailResult(failArray) {
        if (!failArray || failArray.length === 0) {
            return;
        }
        const shows = [];
        failArray.forEach(val => {
            const {categoryName, message} = val
            shows.push(<div key={categoryName}>{categoryName}:{message}</div>)
        })
        notification['error'](
            {
                duration: null,
                placement: 'bottomRight',
                message: '添加失败',
                description: shows
            }
        )
    }

}