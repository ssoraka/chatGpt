package ru.openai.gpt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.file.File;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import ru.openai.gpt.api.OpenAiApiV2;
import ru.openai.gpt.config.GptProps;
import ru.openai.gpt.model.assistant.AssistantV2;
import ru.openai.gpt.model.messages.MessageRequestV2;
import ru.openai.gpt.model.messages.MessageV2;
import ru.openai.gpt.model.runSteps.RunStepV2;
import ru.openai.gpt.model.runs.RunCreateRequestV2;
import ru.openai.gpt.model.runs.RunV2;
import ru.openai.gpt.model.thread.ThreadRequestV2;
import ru.openai.gpt.model.thread.ThreadV2;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Service
public class MyOpenAiService {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10L);
    private final GptProps props;
    private final OpenAiApiV2 apiV2;
    private final OpenAiApi api;
    private final ExecutorService executorService;

    public MyOpenAiService(GptProps props) {
        this.props = props;

        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        OkHttpClient client = OpenAiService.defaultClient(props.getToken(), DEFAULT_TIMEOUT);
        Retrofit retrofit = OpenAiService.defaultRetrofit(client, mapper);
        this.apiV2 = (OpenAiApiV2)retrofit.create(OpenAiApiV2.class);
        this.api = (OpenAiApi)retrofit.create(OpenAiApi.class);
        this.executorService = client.dispatcher().executorService();
    }

    public File uploadFile(String purpose, String content) {
        RequestBody purposeBody = RequestBody.create(MultipartBody.FORM, purpose);
        RequestBody fileBody = RequestBody.create(MediaType.parse("text/plain"), content.getBytes());
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", "filename.txt", fileBody);
        return (File) OpenAiService.execute(this.api.uploadFile(purposeBody, body));
    }

    public AssistantV2 retrieveAssistant(String assistantId) {
        return (AssistantV2)OpenAiService.execute(apiV2.retrieveAssistant(assistantId));
    }

    public ThreadV2 retrieveThread(String threadId) {
        return (ThreadV2)OpenAiService.execute(apiV2.retrieveThread(threadId));
    }

    public ThreadV2 createThread(ThreadRequestV2 request) {
        return (ThreadV2)OpenAiService.execute(apiV2.createThread(request));
    }

    public MessageV2 createMessage(String threadId, MessageRequestV2 messageRequestV2) {
        return (MessageV2)OpenAiService.execute(apiV2.createMessage(threadId, messageRequestV2));
    }

    public OpenAiResponse<MessageV2> retrieveMessages(String threadId) {
        return (OpenAiResponse<MessageV2>)OpenAiService.execute(apiV2.listMessages(threadId));
    }

    public MessageV2 retrieveMessage(String threadId, String messageId) {
        return (MessageV2)OpenAiService.execute(apiV2.retrieveMessage(threadId, messageId));
    }

    public RunV2 createRun(String threadId, RunCreateRequestV2 runCreateRequest) {
        return (RunV2)OpenAiService.execute(apiV2.createRun(threadId, runCreateRequest));
    }

    public RunV2 retrieveRun(String threadId, String runId) {
        return (RunV2)OpenAiService.execute(apiV2.retrieveRun(threadId, runId));
    }

    public OpenAiResponse<RunStepV2> listRunSteps(String threadId, String runId) {
        return (OpenAiResponse<RunStepV2>)OpenAiService.execute(apiV2.listRunSteps(threadId, runId, Map.of()));
    }

    public RunStepV2 retrieveRunStep(String threadId, String runId, String runStepId) {
        return (RunStepV2)OpenAiService.execute(apiV2.retrieveRunStep(threadId, runId, runStepId));
    }

}
