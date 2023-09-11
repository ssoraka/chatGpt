package ru.openai.gpt.controllers;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.openai.gpt.config.GptProps;

import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class Controller {

    private final GptProps gptProps;
    private final OpenAiService openAiService;

    @GetMapping()
    public ResponseEntity<String> get() {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Somebody once told me the world is gonna roll me")
                .model(gptProps.getModel())
                .echo(true)
                .build();
        return ResponseEntity.ok(openAiService.createCompletion(completionRequest).getChoices().stream()
                .map(CompletionChoice::toString)
                .collect(Collectors.joining("\n")));
    }
}
