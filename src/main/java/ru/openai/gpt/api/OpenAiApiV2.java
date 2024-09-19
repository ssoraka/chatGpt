package ru.openai.gpt.api;

import com.theokanning.openai.OpenAiResponse;
import io.reactivex.Single;
import retrofit2.http.*;
import ru.openai.gpt.model.assistant.AssistantV2;
import ru.openai.gpt.model.messages.MessageRequestV2;
import ru.openai.gpt.model.messages.MessageV2;
import ru.openai.gpt.model.runSteps.RunStepV2;
import ru.openai.gpt.model.runs.RunCreateRequestV2;
import ru.openai.gpt.model.runs.RunV2;
import ru.openai.gpt.model.thread.ThreadRequestV2;
import ru.openai.gpt.model.thread.ThreadV2;

import java.util.Map;

public interface OpenAiApiV2 {

    @Headers({"Content-Type: application/json", "OpenAI-Beta: assistants=v2"})
    @GET("/v1/assistants/{assistant_id}")
    Single<AssistantV2> retrieveAssistant(@Path("assistant_id") String var1);

    @Headers({"Content-Type: application/json", "OpenAI-Beta: assistants=v2"})
    @POST("/v1/threads")
    Single<ThreadV2> createThread(@Body ThreadRequestV2 var1);

    @Headers({"Content-Type: application/json", "OpenAI-Beta: assistants=v2"})
    @POST("/v1/threads/{thread_id}/messages")
    Single<MessageV2> createMessage(@Path("thread_id") String var1, @Body MessageRequestV2 var2);

    @Headers({"Content-Type: application/json", "OpenAI-Beta: assistants=v2"})
    @GET("/v1/threads/{thread_id}/messages")
    Single<OpenAiResponse<MessageV2>> listMessages(@Path("thread_id") String var1);

    @Headers({"Content-Type: application/json", "OpenAI-Beta: assistants=v2"})
    @GET("/v1/threads/{thread_id}/messages/{message_id}")
    Single<MessageV2> retrieveMessage(@Path("thread_id") String var1, @Path("message_id") String var2);

    @Headers({"Content-Type: application/json", "OpenAI-Beta: assistants=v2"})
    @POST("/v1/threads/{thread_id}/runs")
    Single<RunV2> createRun(@Path("thread_id") String var1, @Body RunCreateRequestV2 var2);

    @Headers({"Content-Type: application/json", "OpenAI-Beta: assistants=v2"})
    @GET("/v1/threads/{thread_id}/runs/{run_id}")
    Single<RunV2> retrieveRun(@Path("thread_id") String var1, @Path("run_id") String var2);

    @Headers({"Content-Type: application/json", "OpenAI-Beta: assistants=v2"})
    @GET("/v1/threads/{thread_id}/runs/{run_id}/steps")
    Single<OpenAiResponse<RunStepV2>> listRunSteps(@Path("thread_id") String var1, @Path("run_id") String var2, @QueryMap Map<String, String> var3);

    @Headers({"Content-Type: application/json", "OpenAI-Beta: assistants=v2"})
    @GET("/v1/threads/{thread_id}/runs/{run_id}/steps/{step_id}")
    Single<RunStepV2> retrieveRunStep(@Path("thread_id") String var1, @Path("run_id") String var2, @Path("step_id") String var3);
}
