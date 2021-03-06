/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hmily.tac.sqlcompute.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.hmily.repository.spi.entity.HmilyUndoInvocation;
import org.dromara.hmily.tac.sqlcompute.HmilySQLComputeEngine;
import org.dromara.hmily.tac.sqlcompute.exception.SQLComputeException;
import org.dromara.hmily.tac.sqlparser.model.statement.dml.HmilyUpdateStatement;

import java.sql.Connection;

/**
 * Hmily update SQL compute engine.
 *
 * @author zhaojun
 */
@RequiredArgsConstructor
public final class HmilyUpdateSQLComputeEngine implements HmilySQLComputeEngine {
    
    private final HmilyUpdateStatement statement;
    
    @Override
    public HmilyUndoInvocation generateImage(final Connection connection, final String sql) throws SQLComputeException {
        HmilyUndoInvocation undoInvocation = new HmilyUndoInvocation();
        //这里是我的测试验证，写死了
        String revertSql;
        if (sql.contains("order")) {
            String number = sql.substring(sql.indexOf("'") + 1, sql.length() - 1);
            revertSql = "update `order` set status = 3 where number = " + number;
        } else if (sql.contains("account")) {
            revertSql = "update account set balance = balance + 1  where user_id = 10000 ";
        } else {
            revertSql = "update inventory set total_inventory = total_inventory + 1 where product_id = 1";
        }
        undoInvocation.setRevertSql(revertSql);
        undoInvocation.setOriginSql(sql);
        //根据jdbcUrl获取 datasource
        return undoInvocation;
    }
}
