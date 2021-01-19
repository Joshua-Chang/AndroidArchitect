/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package retrofit2;

import static retrofit2.Utils.methodError;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import javax.annotation.Nullable;

abstract class ServiceMethod<T> {
  static <T> ServiceMethod<T> parseAnnotations(Retrofit retrofit, Method method) {
    RequestFactory requestFactory = RequestFactory.parseAnnotations(retrofit, method);//3、解析请求方法的注解

    Type returnType = method.getGenericReturnType();//方法返回值
    if (Utils.hasUnresolvableType(returnType)) {
      throw methodError(
          method,
          "Method return type must not include a type variable or wildcard: %s",
          returnType);
    }
    if (returnType == void.class) {
      throw methodError(method, "Service methods cannot return void.");
    }
    //从ServiceMethod变成其子类HttpServiceMethod 真正从接口方法变为http请求方式
    return HttpServiceMethod.parseAnnotations(retrofit, method, requestFactory);
    //内部确定callAdapter的类型
    //虽然会添加默认callAdapter，但后续仍需处理，因为Retrofit2.8支持了携程，允许suspend标记方法：允许把返回值类型直接写成response体/Bean对象
  }

  abstract @Nullable T invoke(Object[] args);
}
