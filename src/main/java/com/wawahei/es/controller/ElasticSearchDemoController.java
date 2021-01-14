package com.wawahei.es.controller;

import com.wawahei.es.entity.Person;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @description:
 * @author: yanghailang
 * @create: 2021-01-14 10:13
 **/
@RestController
public class ElasticSearchDemoController {

    @Autowired
    private TransportClient client;

    private static final String INDEX = "product";
    private static final String TYPE = "person";

    @GetMapping("test")
    public String test(){
        return "hello";
    }

    @PostMapping("es/add")
    public String add(Person person) throws IOException {
        XContentBuilder content = XContentFactory.jsonBuilder()
                                    .startObject()
                                    .field("name",person.getName())
                                    .field("message",person.getMessage())
                                    .field("job","job")
                                    .field("createTime",new Date())
                                    .endObject();

        IndexResponse response = client.prepareIndex(INDEX,TYPE)
                                        .setSource(content)
                                        .get();

        return response.getId();

    }

    @GetMapping("es/get")
    public Map<String, Object> get(@RequestParam("id") String id){
        GetResponse response = client.prepareGet(INDEX,TYPE,id).get();
        return response.getSource();
    }

    @GetMapping("es/delete")
    public DocWriteResponse.Result delete(@RequestParam("id") String id){
        DeleteResponse deleteResponse = client.prepareDelete(INDEX,TYPE,id).get();
        return deleteResponse.getResult();
    }

    @PostMapping("es/update")
    public DocWriteResponse.Result update(Person person) throws IOException, ExecutionException, InterruptedException {
        UpdateRequest update = new UpdateRequest(INDEX,TYPE,person.getId());

        XContentBuilder builder = XContentFactory.jsonBuilder().startObject();

        if(StringUtils.hasText(person.getName())){
            builder.field("name",person.getName());
        }
        if(StringUtils.hasText(person.getMessage())){
            builder.field("message",person.getMessage());
        }
        if(StringUtils.hasText(person.getJob())){
            builder.field("job",person.getJob());
        }
        builder.endObject();
        update.doc(builder);
        UpdateResponse response = client.update(update).get();

        return response.getResult();
    }
}