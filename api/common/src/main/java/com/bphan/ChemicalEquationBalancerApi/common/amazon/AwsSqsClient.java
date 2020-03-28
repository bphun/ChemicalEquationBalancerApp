package com.bphan.ChemicalEquationBalancerApi.common.amazon;

import java.util.List;

import javax.annotation.PostConstruct;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AwsSqsClient {

    private AmazonSQS sqs;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @Value("${amazonProperties.sqsUrl}")
    private String sqsUrl;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.sqs = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_WEST_1).build();
    }

    public SendMessageResult putMessage(Object message) {
        String messageStr = "";
        SendMessageRequest sendMessageRequest = null;

        try {
            messageStr = objectMapper.writeValueAsString(message);
            sendMessageRequest = new SendMessageRequest().withQueueUrl(sqsUrl).withMessageBody(messageStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sqs.sendMessage(sendMessageRequest);
    }

    public SendMessageResult putMessageStr(String message) {
        SendMessageRequest sendMessageRequest = null;

        try {
            sendMessageRequest = new SendMessageRequest().withQueueUrl(sqsUrl).withMessageBody(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sqs.sendMessage(sendMessageRequest);
    }

    public List<Message> popMessages(int maxNumMessages) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrl)
                .withMaxNumberOfMessages(maxNumMessages).withWaitTimeSeconds(10);

        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        for (Message message : messages) {
            sqs.deleteMessage(
                    new DeleteMessageRequest().withQueueUrl(sqsUrl).withReceiptHandle(message.getReceiptHandle()));
        }

        return messages;
    }

    public Message popMessage() {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrl).withMaxNumberOfMessages(1)
                .withWaitTimeSeconds(10);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        if (messages.size() > 0) {
            Message message = messages.get(0);
            sqs.deleteMessage(
                    new DeleteMessageRequest().withQueueUrl(sqsUrl).withReceiptHandle(message.getReceiptHandle()));
            return message;
        }

        return null;
    }

    public String popMessageStr() {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrl).withMaxNumberOfMessages(1)
                .withWaitTimeSeconds(10);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        if (messages.size() > 0) {
            Message message = messages.get(0);

            sqs.deleteMessage(
                    new DeleteMessageRequest().withQueueUrl(sqsUrl).withReceiptHandle(message.getReceiptHandle()));

            return message.getBody();
        }

        return null;
    }
}