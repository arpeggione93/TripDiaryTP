package com.tripdiary.JCcontroller;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.project.regist.vo.MemberVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tripdiary.JCservice.ReadService;
import com.tripdiary.JCvo.ReadVo;
import com.tripdiary.JCvo.TdLikeVo;

@Controller
public class TdLikeController {

	@Inject
	ReadService service;

	@RequestMapping(value = "/likeClick", method = RequestMethod.GET)
	public String tdlike(TdLikeVo tdlikeVo, ReadVo readVo, Model model, HttpSession session) throws Exception {
		// �쁽�옱 濡쒓렇�씤 硫ㅻ쾭 �솗�씤
		MemberVo memberVo = (MemberVo) session.getAttribute("authInfo");
		System.out.println("readView(memberVo) : " + memberVo.toString());
		model.addAttribute("memberVo", memberVo);

		if (tdlikeVo != null) {
			System.out.println("pickVo : " + tdlikeVo.toString());
		} else {
			System.out.println("�꼸");
		}

		// 留뚯빟 memberVo媛� null�씠 �븘�땲怨� member�쓽 memberNum怨� readCmd�쓽 boardNum�씠 媛숇떎硫� ,
		// 濡쒓렇�씤�긽�깭�뿉�꽌 �룞�옉 �솗�씤
		String url = "";

		if (memberVo != null) {
			System.out.println(memberVo.toString() + "�씠寃� �뼵�젣 戮묓엳�깘");

			MemberActCntCmd memberActCntCmd = new MemberActCntCmd(readVo.getBoardNum(), readVo.getBoardMemberNum(),
					memberVo.getMemberNum(), "like");

			TdLikeVo tdLikeCheck = service.tdLikeCheck(tdlikeVo);
			model.addAttribute("tdLikeCheck", tdLikeCheck);
			// �빐�떦 �쉶�썝�쓽 memberNum�쑝濡� 議고쉶�븳 tdLike�뀒�씠釉붿쓣 List濡� 媛��졇���꽌
			// 由ъ뒪�듃�씪�븣 �넻吏몃줈 鍮꾧탳�븷�븣 is empty -- 怨듬��빐�빞�븿
			if (tdLikeCheck != null) {
				// �븞鍮꾩뼱�엳�쑝硫� �궘�젣
				service.deleteTdlike(tdlikeVo);
				memberActCntCmd.setUpdateType("delete");
				
				System.out.println("�궘�젣 �썑 readCmd(�떦�뿰�엳 �뾾寃좎컡..) : " + readVo);
				System.out.println("delete : " + tdLikeCheck.toString());
				System.out.println("memberActCntCmd.delete : " + memberActCntCmd.toString());

				url = "redirect:/readView?boardNum=" + tdLikeCheck.getBoardNum() + "&memberNum="
						+ tdLikeCheck.getMemberNum();
			} else {
				// 鍮꾩뼱�엳�쑝硫� insert
				service.insertTdlike(tdlikeVo);
				memberActCntCmd.setUpdateType("insert");
				
				System.out.println("李쒗븳 �썑 readCmd : " + readVo);
				System.out.println("readCmd.insert : " + tdlikeVo.toString());
				System.out.println("memberActCntCmd.insert : " + memberActCntCmd.toString());

				url = "redirect:/readView?boardNum=" + tdlikeVo.getBoardNum() + "&memberNum=" + tdlikeVo.getMemberNum();
			}
			service.boardTotalLike(memberActCntCmd);
			service.memberActCntCmd(memberActCntCmd);
			service.memberLikeReceiveCnt(memberActCntCmd);
			// return "redirect:/readView?boardNum=" + readCmd.getBoardNum();
		}
		// delete �룞�옉 �썑
		return url;

//	// pickCheck
	}

}