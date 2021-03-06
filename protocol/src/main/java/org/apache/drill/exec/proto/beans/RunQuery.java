/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from protobuf

package org.apache.drill.exec.proto.beans;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.Input;
import com.dyuproject.protostuff.Message;
import com.dyuproject.protostuff.Output;
import com.dyuproject.protostuff.Schema;

public final class RunQuery implements Externalizable, Message<RunQuery>, Schema<RunQuery>
{

    public static Schema<RunQuery> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static RunQuery getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final RunQuery DEFAULT_INSTANCE = new RunQuery();

    
    private QueryResultsMode resultsMode;
    private QueryType type;
    private String plan;
    private List<PlanFragment> fragments;
    private PreparedStatementHandle preparedStatementHandle;

    public RunQuery()
    {
        
    }

    // getters and setters

    // resultsMode

    public QueryResultsMode getResultsMode()
    {
        return resultsMode == null ? QueryResultsMode.STREAM_FULL : resultsMode;
    }

    public RunQuery setResultsMode(QueryResultsMode resultsMode)
    {
        this.resultsMode = resultsMode;
        return this;
    }

    // type

    public QueryType getType()
    {
        return type == null ? QueryType.SQL : type;
    }

    public RunQuery setType(QueryType type)
    {
        this.type = type;
        return this;
    }

    // plan

    public String getPlan()
    {
        return plan;
    }

    public RunQuery setPlan(String plan)
    {
        this.plan = plan;
        return this;
    }

    // fragments

    public List<PlanFragment> getFragmentsList()
    {
        return fragments;
    }

    public RunQuery setFragmentsList(List<PlanFragment> fragments)
    {
        this.fragments = fragments;
        return this;
    }

    // preparedStatementHandle

    public PreparedStatementHandle getPreparedStatementHandle()
    {
        return preparedStatementHandle;
    }

    public RunQuery setPreparedStatementHandle(PreparedStatementHandle preparedStatementHandle)
    {
        this.preparedStatementHandle = preparedStatementHandle;
        return this;
    }

    // java serialization

    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }

    // message method

    public Schema<RunQuery> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public RunQuery newMessage()
    {
        return new RunQuery();
    }

    public Class<RunQuery> typeClass()
    {
        return RunQuery.class;
    }

    public String messageName()
    {
        return RunQuery.class.getSimpleName();
    }

    public String messageFullName()
    {
        return RunQuery.class.getName();
    }

    public boolean isInitialized(RunQuery message)
    {
        return true;
    }

    public void mergeFrom(Input input, RunQuery message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 1:
                    message.resultsMode = QueryResultsMode.valueOf(input.readEnum());
                    break;
                case 2:
                    message.type = QueryType.valueOf(input.readEnum());
                    break;
                case 3:
                    message.plan = input.readString();
                    break;
                case 4:
                    if(message.fragments == null)
                        message.fragments = new ArrayList<PlanFragment>();
                    message.fragments.add(input.mergeObject(null, PlanFragment.getSchema()));
                    break;

                case 5:
                    message.preparedStatementHandle = input.mergeObject(message.preparedStatementHandle, PreparedStatementHandle.getSchema());
                    break;

                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, RunQuery message) throws IOException
    {
        if(message.resultsMode != null)
             output.writeEnum(1, message.resultsMode.number, false);

        if(message.type != null)
             output.writeEnum(2, message.type.number, false);

        if(message.plan != null)
            output.writeString(3, message.plan, false);

        if(message.fragments != null)
        {
            for(PlanFragment fragments : message.fragments)
            {
                if(fragments != null)
                    output.writeObject(4, fragments, PlanFragment.getSchema(), true);
            }
        }


        if(message.preparedStatementHandle != null)
             output.writeObject(5, message.preparedStatementHandle, PreparedStatementHandle.getSchema(), false);

    }

    public String getFieldName(int number)
    {
        switch(number)
        {
            case 1: return "resultsMode";
            case 2: return "type";
            case 3: return "plan";
            case 4: return "fragments";
            case 5: return "preparedStatementHandle";
            default: return null;
        }
    }

    public int getFieldNumber(String name)
    {
        final Integer number = __fieldMap.get(name);
        return number == null ? 0 : number.intValue();
    }

    private static final java.util.HashMap<String,Integer> __fieldMap = new java.util.HashMap<String,Integer>();
    static
    {
        __fieldMap.put("resultsMode", 1);
        __fieldMap.put("type", 2);
        __fieldMap.put("plan", 3);
        __fieldMap.put("fragments", 4);
        __fieldMap.put("preparedStatementHandle", 5);
    }
    
}
