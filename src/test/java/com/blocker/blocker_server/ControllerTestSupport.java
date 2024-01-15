package com.blocker.blocker_server;

import com.blocker.blocker_server.board.controller.BoardController;
import com.blocker.blocker_server.bookmark.domain.Bookmark;
import com.blocker.blocker_server.chat.controller.ChatController;
import com.blocker.blocker_server.contract.controller.CancelContractController;
import com.blocker.blocker_server.contract.controller.ContractController;
import com.blocker.blocker_server.sign.controller.AgreementSignController;
import com.blocker.blocker_server.sign.controller.CancelSignController;
import com.blocker.blocker_server.sign.service.AgreementSignService;
import com.blocker.blocker_server.sign.service.CancelSignServiceSupport;
import com.blocker.blocker_server.signature.controller.SignatureController;
import com.blocker.blocker_server.user.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        BoardController.class,
        Bookmark.class,
        ChatController.class,
        ContractController.class,
        CancelContractController.class,
        AgreementSignController.class,
        CancelSignController.class,
        SignatureController.class,
        UserController.class
})
public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
