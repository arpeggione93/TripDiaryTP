package com.tripdiary.JCcontroller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.project.regist.vo.MemberVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tripdiary.HSvo.PickVo;
import com.tripdiary.HSvo.TagVo;
import com.tripdiary.JCservice.ReadService;
import com.tripdiary.JCvo.BoardImgVo;
import com.tripdiary.JCvo.ReadVo;
import com.tripdiary.JCvo.ReplyCommand;
import com.tripdiary.JCvo.ReplyVo;
import com.tripdiary.JCvo.TdLikeVo;

@Controller
public class ReadController {

	private static final Logger logger = LoggerFactory.getLogger(ReadController.class);

	@Inject
	ReadService service;

	// 寃뚯떆�뙋 �긽�꽭 蹂닿린 + �뙎湲� 紐⑸줉
	@RequestMapping(value = "/readView", method = RequestMethod.GET)
	public String read(ReadVo readVo, ReadViewCmd readCmd, Model model, HttpSession session)
			throws Exception {
		logger.info("read");
		System.out.println(readCmd.toString());

		// �쁽�옱 濡쒓렇�씤 硫ㅻ쾭 �솗�씤
		MemberVo memberVo = (MemberVo) session.getAttribute("authInfo");
		if(memberVo != null) {
			System.out.println("readView(memberVo) : " + memberVo.toString());
		}
		model.addAttribute("memberVo", memberVo);

		// 寃뚯떆湲� 紐⑸줉 - boardNum, memberNum, nickname, profileVo
		ReadVo read = service.read(readCmd.getBoardNum());
		System.out.println(read.toString());
		model.addAttribute("read", read);

		// �빐�떦 寃뚯떆湲� �뙎湲� 紐⑸줉 - replyVo, nickname
		List<ReplyCommand> replyList = service.replyList(readCmd.getBoardNum());
		System.out.println(replyList.toString());
		model.addAttribute("replyList", replyList);

		// ���몴�궗吏� �씠誘몄� 紐⑸줉
		BoardImgVo thumbnailImg = service.ThumbnailImg(read.getBoardNum());
		System.out.println(thumbnailImg.toString());
		model.addAttribute("thumbnailImg", thumbnailImg);
		
		// 蹂대뱶 �씠誘몄� 紐⑸줉
		List<BoardImgVo> boardImgList = service.BoardImgList(read.getBoardNum());
		System.out.println(boardImgList.toString());
		model.addAttribute("boardImgList", boardImgList);

		// �깭洹�
		List<TagVo> tagList = service.tagList(read.getBoardNum());
		System.out.println("tagList : " + tagList.toString());
		model.addAttribute("tagList", tagList);

		// 李� �쉶�썝 �솗�씤
		if(memberVo != null) {
			PickVo pickVo = new PickVo(memberVo.getMemberNum(), readCmd.getBoardNum());
			System.out.println("pickVo : " + pickVo.toString());
			PickVo pickCheck = service.pickCheck(pickVo);

			if (pickCheck == null) {
				System.out.println("pickCheck �뾾�쓬");
			} else {
				System.out.println("pickCheck : " + pickCheck.toString());
			}
	
			model.addAttribute("pickCheck", pickCheck);

			// 醫뗭븘�슂 �쉶�썝 �솗�씤
			TdLikeVo tdLikeVo = new TdLikeVo(memberVo.getMemberNum(), readCmd.getBoardNum());
			System.out.println("tdLikeVo : " + tdLikeVo.toString());
	
			TdLikeVo tdLikeCheck = service.tdLikeCheck(tdLikeVo);
	
			if (tdLikeCheck == null) {
				System.out.println("tdLikeCheck �뾾�쓬");
			} else {
				System.out.println("tdLikeCheck : " + tdLikeCheck.toString());
			}
	
			model.addAttribute("tdLikeCheck", tdLikeCheck);
		}

		return "readView";
	}
	
	// 寃뚯떆湲� �궘�젣
		@RequestMapping(value = "/delete", method = RequestMethod.GET)
		public String delete(ReadVo readVo, ReadViewCmd readCmd, Model model, HttpSession session) throws Exception {

			logger.info("delete");

			// hidden�뿉 �뱾�뼱媛��뒗嫄� - �궘�젣�빐�빞�븯�굹??
			ReadVo read = service.read(readCmd.getBoardNum());
			System.out.println(read.toString());
			model.addAttribute("tdLikeCnt �솢�슜�븷 read", read);

			// �쁽�옱 濡쒓렇�씤 �맂 �쉶�썝�씤吏� �븘�땶吏� �뙆�븙 �썑 蹂몄씤湲��씠硫� �궘�젣吏꾪뻾, �븘�땲硫� 蹂몄씤 寃뚯떆湲��씠 �븘�땲�씪�뒗 �븞�궡硫섑듃
			MemberVo memberVo = (MemberVo) session.getAttribute("authInfo");

			MemberActCntCmd memberActCntCmd = new MemberActCntCmd(readCmd.getBoardNum(), memberVo.getMemberNum(),
					memberVo.getMemberNum(), "deleteBoard");

			if (memberVo != null) {
				System.out.println("delete(memberVo) : " + memberVo.toString());
				model.addAttribute("memberVo", memberVo);
				
				service.delete(readCmd.getBoardNum());
				
				memberActCntCmd.setTdLikeCnt(read.getTdLikeCnt());
				memberActCntCmd.setUpdateType("delete");
				service.deleteReceiveCnt(memberActCntCmd);
				System.out.println("deleteReceiveCnt :memberActCntCmd.delete : " + memberActCntCmd.toString());
			}
			return "redirect:/";
		}
	
	// �뙎湲� �옉�꽦
	@RequestMapping(value = "/replyWrite", method = RequestMethod.POST)
	public String replyWrite(ReplyVo replyVo, ReadViewCmd readCmd, Model model, HttpSession session) throws Exception {
		logger.info("reply Write");
		if(replyVo.getMemberNum() == 0) {
			model.addAttribute("msg", "로그인 후 이용해주세요!");
    		model.addAttribute("url", "/login/");
    		return "/return/alert";
		}
		System.out.println("�옉�꽦�븯怨� �궃 �썑 : " + readCmd.toString());

		// hidden�뿉 �뱾�뼱媛��뒗嫄� - �궘�젣�빐�빞�븯�굹??
		ReadVo read = service.read(readCmd.getBoardNum());
		System.out.println(read.toString());
		model.addAttribute("read", read);

		// �쁽�옱 濡쒓렇�씤 硫ㅻ쾭 �솗�씤 - �궘�젣�빐�빞�븿
		MemberVo memberVo = (MemberVo) session.getAttribute("authInfo");
		System.out.println("replyWrite(memberVo) : " + memberVo.toString());
		model.addAttribute("memberVo", memberVo);

		MemberActCntCmd memberActCntCmd = new MemberActCntCmd(readCmd.getBoardNum(), readCmd.getMemberNum(),
				memberVo.getMemberNum(), "reply");

		// �옉�꽦
		service.replyWrite(replyVo);
		System.out.println(replyVo.toString());

		memberActCntCmd.setUpdateType("insert");
		service.replyActCnt(memberActCntCmd);
		System.out.println("reply :memberActCntCmd.insert : " + memberActCntCmd.toString());

		return "redirect:/readView?boardNum=" + replyVo.getBoardNum();
	}

	// �뙎湲� �닔�젙 GET
	@RequestMapping(value = "/replyUpdate", method = RequestMethod.GET)
	public String getReplyUpdate(ReplyVo replyVo, Model model, ReadViewCmd readCmd, HttpSession session)
			throws Exception {
		logger.info("reply Update");

		// hidden�뿉 �뱾�뼱媛��뒗嫄� - �궘�젣�빐�빞�븯�굹??
		ReadVo read = service.read(readCmd.getBoardNum());
		System.out.println(read.toString());
		model.addAttribute("read", read);

		// �쁽�옱 濡쒓렇�씤 硫ㅻ쾭 �솗�씤 - �궘�젣�빐�빞�븿
		MemberVo memberVo = (MemberVo) session.getAttribute("authInfo");
		System.out.println("replyUpdate(memberVo) : " + memberVo.toString());
		model.addAttribute("memberVo", memberVo);

		System.out.println("replyUpdate,replyVo : " + replyVo.toString());
		ReplyVo replyVoCheck = service.selectReply(replyVo.getReplyNum());
		System.out.println(replyVoCheck.toString());

		// �닔�젙�븯湲곗쐞�빐 replyVo�뿉�꽌 �젙蹂� 媛��졇�삤湲�
		model.addAttribute("selectReply", replyVoCheck);

		return "replyUpdate";
	}

	// �뙎湲� �닔�젙 POST
	@RequestMapping(value = "/replyUpdate", method = RequestMethod.POST)
	public String replyUpdate(ReplyVo replyVo, Model model, ReadViewCmd readCmd, HttpSession session) throws Exception {
		logger.info("reply Update");

		System.out.println("replyUpdate,�꽬�꽮�뀋");

		// hidden�뿉 �뱾�뼱媛��뒗嫄� - �궘�젣�빐�빞�븯�굹??
		ReadVo read = service.read(readCmd.getBoardNum());
		System.out.println(read.toString());
		model.addAttribute("read", read);

		// �쁽�옱 濡쒓렇�씤 硫ㅻ쾭 �솗�씤 - �궘�젣�빐�빞�븿
		MemberVo memberVo = (MemberVo) session.getAttribute("authInfo");
		System.out.println(memberVo.toString());
		model.addAttribute("memberVo", memberVo);

		// �뙎湲� �옉�꽦 �뾽�뜲�씠�듃
		service.replyUpdate(replyVo);
		System.out.println(replyVo.toString());

		return "redirect:/readView?boardNum=" + replyVo.getBoardNum();
	}

	// �뙎湲� �궘�젣 POST
	@RequestMapping(value = "/replyDelete", method = RequestMethod.GET)
	public String replyDelete(ReplyVo replyVo, Model model, ReadViewCmd readCmd, HttpSession session) throws Exception {
		logger.info("reply Delete");

		System.out.println("replyDelete �뱾�뼱�솕�떎");

		// hidden�뿉 �뱾�뼱媛��뒗嫄� - �궘�젣�빐�빞�븯�굹??
		ReadVo read = service.read(readCmd.getBoardNum());
		System.out.println(read.toString());
		model.addAttribute("read", read);

		// �쁽�옱 濡쒓렇�씤 硫ㅻ쾭 �솗�씤 - �궘�젣�빐�빞�븿
		MemberVo memberVo = (MemberVo) session.getAttribute("authInfo");
		System.out.println(memberVo.toString());
		model.addAttribute("memberVo", memberVo);

		MemberActCntCmd memberActCntCmd = new MemberActCntCmd(readCmd.getBoardNum(), readCmd.getMemberNum(),
				memberVo.getMemberNum(), "reply");

		// �뙎湲� �궘�젣 �뾽�뜲�씠�듃
		service.replyDelete(replyVo);
		System.out.println(replyVo.toString());

		memberActCntCmd.setUpdateType("delete");
		System.out.println("reply : memberActCntCmd.insert : " + memberActCntCmd.toString());

		service.replyActCnt(memberActCntCmd);

		return "redirect:/readView?boardNum=" + replyVo.getBoardNum();
	}

}
