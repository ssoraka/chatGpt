package ru.openai.gpt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiResponse;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import ru.openai.gpt.api.OpenAiApiV2;
import ru.openai.gpt.config.GptProps;
import ru.openai.gpt.model.assistant.AssistantV2;
import ru.openai.gpt.model.messages.MessageV2;
import ru.openai.gpt.model.runs.RunCreateRequestV2;
import ru.openai.gpt.model.runs.RunV2;
import ru.openai.gpt.model.thread.ThreadRequestV2;
import ru.openai.gpt.model.thread.ThreadV2;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

@Service
public class MyOpenAiService {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10L);
    private final GptProps props;
    private final OpenAiApiV2 api;
    private final ExecutorService executorService;

    public MyOpenAiService(GptProps props) {
        this.props = props;

        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        OkHttpClient client = OpenAiService.defaultClient(props.getToken(), DEFAULT_TIMEOUT);
        Retrofit retrofit = OpenAiService.defaultRetrofit(client, mapper);
        this.api = (OpenAiApiV2)retrofit.create(OpenAiApiV2.class);
        this.executorService = client.dispatcher().executorService();
    }

    public AssistantV2 retrieveAssistant(String assistantId) {
        return (AssistantV2)OpenAiService.execute(api.retrieveAssistant(assistantId));
    }

    public ThreadV2 createThread(ThreadRequestV2 request) {
        return (ThreadV2)OpenAiService.execute(api.createThread(request));
    }

    public OpenAiResponse<MessageV2> retrieveMessages(String threadId) {
        return (OpenAiResponse<MessageV2>)OpenAiService.execute(api.listMessages(threadId));
    }

    public RunV2 createRun(String threadId, RunCreateRequestV2 runCreateRequest) {
        return (RunV2)OpenAiService.execute(api.createRun(threadId, runCreateRequest));
    }

    public RunV2 retrieveRun(String threadId, String runId) {
        return (RunV2)OpenAiService.execute(api.retrieveRun(threadId, runId));
    }

}
