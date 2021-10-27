package com.example.thymelaefexam;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Slf4j
public class ThymeleafExam {

    /**
     * Template Engine.
     * The cost of creation is very high. This is thread safe.
     */
    private static TemplateEngine engine = null;
    
    @Data
    @AllArgsConstructor
    private static class MsgLine {
        int no;
        boolean status;
        String msg;
    }
    
    public static void main(String[] args) {
        int no = 1;
        List<MsgLine> msgList = new ArrayList<>();
        msgList.add(new MsgLine(no++, true,"小麦粉をボールに入れる"));
        msgList.add(new MsgLine(no++, true,"砂糖をボールに入れる"));
        msgList.add(new MsgLine(no++, true,"牛乳をボールに入れる"));
        msgList.add(new MsgLine(no++, false,"シナモンをボールに入れる→無かったので省略"));
        msgList.add(new MsgLine(no++, true,"よく混ぜる"));
        msgList.add(new MsgLine(no++, true,"フライパンで焼く"));
        msgList.add(new MsgLine(no++, true,"皿に盛る"));
        msgList.add(new MsgLine(no++, true,"はちみつをかける"));
        
        Map<String, Object> contents = new HashMap<>();
        contents.put("title", "Task list");
        contents.put("lines", msgList);
        
        try {
            File outFile = File.createTempFile("report", ".html");
            log.info("Start");
            log.info("CREATE RPOERT {}", outFile.getAbsolutePath());
            execute(new FileWriter(outFile, Charset.forName("UTF-8")), "template", contents);
            log.info("End");
        } catch (IOException ex) {
            log.error("ERROR", ex);
        }
    }
    
    public static void execute(final Writer outputWriter, final String template, final Map<String, Object> contents) {
        // Initialize Template Engine
        if (null == engine) {
            engine = initializeTemplateEngine();
        }
        // Create the thymeleaf context
        final IContext ctx = makeContext(contents);
        
        // Create html
        engine.process(template, ctx, new BufferedWriter(outputWriter));
    }

    private static TemplateEngine initializeTemplateEngine() {
        // Create Engine
        final TemplateEngine templateEngine = new TemplateEngine();
        // Create Template Resolver (Search tempaltes under the classpath.）
        final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        // Options of resolver
        resolver.setTemplateMode("HTML");
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        
        // Set the resolver on the template Engine.
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }

    /**
     * 
     * @param contents
     * @return 
     */
    private static IContext makeContext(final Map<String, Object> contents) {
        final Context ctx = new Context();
        ctx.setVariables(contents);
        return ctx;
    }    
}
