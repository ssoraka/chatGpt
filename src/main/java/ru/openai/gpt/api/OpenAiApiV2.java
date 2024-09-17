package ru.openai.gpt.api;

import io.reactivex.Single;
import retrofit2.http.*;
import ru.openai.gpt.model.assistant.AssistantV2;
import ru.openai.gpt.model.runs.RunCreateRequestV2;
import ru.openai.gpt.model.runs.RunV2;
import ru.openai.gpt.model.thread.ThreadRequestV2;
import ru.openai.gpt.model.thread.ThreadV2;

public interface OpenAiApiV2 {

    @Headers({"OpenAI-Beta: assistants=v2"})
    @GET("/v1/assistants/{assistant_id}")
    Single<AssistantV2> retrieveAssistant(@Path("assistant_id") String var1);

    @Headers({"OpenAI-Beta: assistants=v2"})
    @POST("/v1/threads")
    Single<ThreadV2> createThread(@Body ThreadRequestV2 var1);

    @Headers({"OpenAI-Beta: assistants=v2"})
    @GET("/v1/threads/{thread_id}")
    Single<ThreadV2> retrieveThread(@Path("thread_id") String var1);

    @Headers({"OpenAI-Beta: assistants=v2"})
    @POST("/v1/threads/{thread_id}/runs")
    Single<RunV2> createRun(@Path("thread_id") String var1, @Body RunCreateRequestV2 var2);

    @Headers({"OpenAI-Beta: assistants=v2"})
    @GET("/v1/threads/{thread_id}/runs/{run_id}")
    Single<RunV2> retrieveRun(@Path("thread_id") String var1, @Path("run_id") String var2);

}
