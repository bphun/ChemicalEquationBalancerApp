package com.bphan.ChemicalEquationBalancerApi.common.amazon;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private Logger logger = Logger.getLogger(AwsSqsClient.class.getName());

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

            logger.log(Level.INFO, "Added message to SQS queue");
        } catch (Exception e) {
            logger.warning(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return sqs.sendMessage(sendMessageRequest);
    }

    public SendMessageResult putMessageStr(String message) {
        return putMessage(message);
    }

    public List<Message> popMessages(int maxNumMessages) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(sqsUrl)
                .withMaxNumberOfMessages(maxNumMessages).withWaitTimeSeconds(10);

        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

        if (messages.size() > 0) {
            logger.log(Level.INFO, "Popped " + messages.size() + " messages from SQS queue");

            for (Message message : messages) {
                try {
                    sqs.deleteMessage(
                            new DeleteMessageRequest().withQueueUrl(sqsUrl).withReceiptHandle(message.getReceiptHandle()));
                } catch (Exception e) {
                    logger.warning(e.getLocalizedMessage());
                }
            }
        }

        return null;
    }

    public Message popMessage() {
        List<Message> messages = popMessages(1);

        if (messages != null && messages.size() > 0) {
            return messages.get(0);
        }

        return null;
    }

    public String popMessageStr() {
        Message message = popMessage();

        if (message != null) {
            return message.getBody();
        }

        return "";
    }
}