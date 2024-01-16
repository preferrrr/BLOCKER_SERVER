package com.blocker.blocker_server;

import com.blocker.blocker_server.Image.controller.ImageController;
import com.blocker.blocker_server.Image.service.ImageService;
import com.blocker.blocker_server.board.controller.BoardController;
import com.blocker.blocker_server.board.service.BoardService;
import com.blocker.blocker_server.bookmark.controller.BookmarkController;
import com.blocker.blocker_server.bookmark.domain.Bookmark;
import com.blocker.blocker_server.bookmark.service.BookmarkService;
import com.blocker.blocker_server.chat.controller.ChatController;
import com.blocker.blocker_server.chat.service.ChatService;
import com.blocker.blocker_server.contract.controller.CancelContractController;
import com.blocker.blocker_server.contract.controller.ContractController;
import com.blocker.blocker_server.contract.service.CancelContractService;
import com.blocker.blocker_server.contract.service.ContractService;
import com.blocker.blocker_server.sign.controller.AgreementSignController;
import com.blocker.blocker_server.sign.controller.CancelSignController;
import com.blocker.blocker_server.sign.service.AgreementSignService;
import com.blocker.blocker_server.sign.service.CancelSignService;
import com.blocker.blocker_server.sign.service.CancelSignServiceSupport;
import com.blocker.blocker_server.signature.controller.SignatureController;
import com.blocker.blocker_server.signature.service.SignatureService;
import com.blocker.blocker_server.user.controller.UserController;
import com.blocker.blocker_server.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        BoardController.class,
        BookmarkController.class,
        ChatController.class,
        ContractController.class,
        CancelContractController.class,
        AgreementSignController.class,
        CancelSignController.class,
        SignatureController.class,
        UserController.class,
        ImageController.class
})
public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected BoardService boardService;

    @MockBean
    protected BookmarkService bookmarkService;

    @MockBean
    protected ChatService chatService;

    @MockBean
    protected CancelContractService cancelContractService;

    @MockBean
    protected ContractService contractService;

    @MockBean
    protected ImageService imageService;

    @MockBean
    protected AgreementSignService agreementSignService;

    @MockBean
    protected CancelSignService cancelSignService;

    @MockBean
    protected SignatureService signatureService;

    @MockBean
    protected UserService userService;



}
