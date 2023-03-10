package com.store.contorller;

import javax.servlet.*;
import javax.servlet.http.*;

import com.code.contorller.CodeServlet;
import com.code.model.service.CodeService;
import com.emp.model.Employee.dao.impl.EmployeeJDBCDAO;
import com.member.model.Member.pojo.Member;
import com.member.model.service.MemberService;
import com.store.model.Store.pojo.Store;
import com.store.model.service.StoreService;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutOneTime;
import org.json.simple.JSONArray;

import javax.servlet.annotation.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
@WebServlet("/Member/StoreServlet")
public class StoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        Integer storeId = (Integer) request.getSession().getAttribute("storeId");
        Integer memid = (Integer) request.getSession().getAttribute("memId");
        Integer empId = (Integer) request.getSession().getAttribute("empId");
        //  update
        if ("update".equals(action)) {
            List<String> errorMsgs = new LinkedList<String>();
            request.setAttribute("errorMsgs", errorMsgs);
            /*********************** 1.?????????????????? - ??????????????????????????? *************************/
            String memname = request.getParameter("MEM_NAME");
            String memnameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
            if (memname == null || memname.trim().length() == 0) {
                errorMsgs.add("??????: ????????????");
            } else if (!memname.trim().matches(memnameReg)) { // ??????????????????(???)?????????(regular-expression)
                errorMsgs.add("??????: ???????????????????????????????????????_ , ??????????????????2???10??????");
            }
            String memacc = request.getParameter("MEM_ACC").trim();
            if (memacc == null || memacc.trim().length() == 0) {
                errorMsgs.add("??????????????????");
            }
            String memrecipient = request.getParameter("MEM_RECIPIENT").trim();
            if (memrecipient == null || memrecipient.trim().length() == 0) {
                errorMsgs.add("????????????????????????");
            }
            String memtwid = request.getParameter("MEM_TW_ID").trim();
            String memtwidReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
            if (memtwid == null || memtwid.trim().length() == 0) {
                errorMsgs.add("???????????????????????????");
            } else if (!memtwid.trim().matches(memtwidReg)) { // ??????????????????(???)?????????(regular-expression)
                errorMsgs.add("???????????????????????????");
            }
            java.sql.Date membirthday = null;
            try {
                membirthday = java.sql.Date.valueOf(request.getParameter("MEM_BIRTHDAY").trim());
            } catch (IllegalArgumentException e) {
                membirthday = new java.sql.Date(System.currentTimeMillis());
                errorMsgs.add("???????????????!");
            }
            String memphone = request.getParameter("MEM_PHONE").trim();
            if (memphone == null || memphone.trim().length() == 0) {
                errorMsgs.add("????????????????????????");
            }
            Integer mempostalcode = Integer.valueOf(request.getParameter("MEM_POSTAL_CODE").trim());
            if (mempostalcode == null) {
                errorMsgs.add("????????????????????????");
            }
            String memcity = request.getParameter("MEM_CITY").trim();
            if (memcity == null || memcity.trim().length() == 0) {
                errorMsgs.add("??????????????????");
            }
            String memdistrict = request.getParameter("MEM_DISTRICT").trim();
            if (memdistrict == null || memdistrict.trim().length() == 0) {
                errorMsgs.add("??????????????????");
            }
            String memaddress = request.getParameter("MEM_ADDRESS").trim();
            if (memaddress == null || memaddress.trim().length() == 0) {
                errorMsgs.add("??????????????????");
            }
            String memmail = request.getParameter("MEM_MAIL").trim();
            if (memmail == null || memmail.trim().length() == 0) {
                errorMsgs.add("????????????????????????");
            }
            String memtext = request.getParameter("MEM_TEXT").trim();
            byte[] mempic = request.getPart("MEM_PIC").getInputStream().readAllBytes();
            Member Member = new Member();
            Member.setMemName(memname);
            Member.setMemAcc(memacc);
            Member.setMemRecipient(memrecipient);
            Member.setMemTwId(memtwid);
            Member.setMemBirthday(membirthday);
            Member.setMemPhone(memphone);
            Member.setMemPostalCode(mempostalcode);
            Member.setMemCity(memcity);
            Member.setMemDistrict(memdistrict);
            Member.setMemAddress(memaddress);
            Member.setMemMail(memmail);
            Member.setMemText(memtext);
            Member.setMemPic(mempic);
            if (!errorMsgs.isEmpty()) {
                request.setAttribute("Member", Member);
                RequestDispatcher failureView = request
                        .getRequestDispatcher("/front-end/Member/member/memberIInfo.jsp");
                failureView.forward(request, response);
                return;
            }
            /*************************** 2.?????????????????? ***************************************/
            MemberService memSvc = new MemberService();
            Member = memSvc.updateMem(memid, memname, memacc, memrecipient, memtwid, membirthday, memphone, mempostalcode,
                    memcity, memdistrict, memaddress, memmail, memtext, mempic);
            /*************************** 3.????????????,????????????(Send the Success view) ***********/
            String url = "/index.jsp";
            RequestDispatcher successView = request.getRequestDispatcher(url);
            successView.forward(request, response);
        }
        //  search1
        if ("Search".equals(action)) {
            List<String> errorMsgs = new LinkedList<String>();
            request.setAttribute("errorMsgs", errorMsgs);
            /*************************** 1.?????????????????? - ??????????????????????????? **********************/
            String srh = request.getParameter("search");
            String srhms = request.getParameter("Search");
            if (srh == null || (srh.trim()).length() == 0) {
                errorMsgs.add("????????????");
            }
            if (!errorMsgs.isEmpty()) {
                RequestDispatcher failureView = request.getRequestDispatcher("/index.jsp");
                failureView.forward(request, response);
                return;// ????????????
            }
            /*************************** 2.?????????????????? *****************************************/
            if ("searchMember".equals(srhms)) {
                MemberService memSvc = new MemberService();
                List<Member> Member = memSvc.getMember(srh);
                if (Member == null) {
                    errorMsgs.add("????????????");
                    RequestDispatcher failureView = request.getRequestDispatcher("/index.jsp");
                    failureView.forward(request, response);
                    return;// ????????????
                }
                /*************************** 3.????????????,????????????(Send the Success view) *************/
                request.setAttribute("memId", Member);
                String url = "/front-end/Member/member/searchMember.jsp";
                RequestDispatcher successView = request.getRequestDispatcher(url);
                successView.forward(request, response);
            }
        }
        if ("MemberInfo".equals(action)) {
            MemberService memSvc = new MemberService();
            Member Member = memSvc.meminfo(memid);
            request.setAttribute("Member", Member);
            String url = "/front-end/Member/member/memberIInfo.jsp";
            RequestDispatcher successView = request.getRequestDispatcher(url);
            successView.forward(request, response);
        }
        //????????????????????????
        if ("reviewStore".equals(action)) {
            Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
            request.setAttribute("errorMsgs", errorMsgs);
            CodeService codeService = new CodeService();
            Integer root = codeService.Coupon_root(empId);
            JSONArray empaccs = codeService.empacc();
            StoreService storeService = new StoreService();
            JSONArray json = storeService.empstore(empId);
            String empacc = new EmployeeJDBCDAO().findByEMP_ID(empId).getEmpAcc();
            String url = "/back-end/store/reviewStore.jsp";
            String errorString = "";
            if (json.size() == 0) {
                if (root == 4 || root == 1) {
                    errorString = "?????????" + empacc + "????????????????????????????????????????????????????????????";
                } else {
                    errorString = "?????????" + empacc + "?????????????????????????????????????????????????????????????????????????????????";
                }
                errorMsgs.put("error", errorString);
            }
            request.setAttribute("empaccs", empaccs);
            request.setAttribute("root", root);
            request.setAttribute("list_out", json);
            RequestDispatcher successView = request.getRequestDispatcher(url);
            successView.forward(request, response);
        }
        //????????????
        if ("empTo".equals(action)) {
            storeId = Integer.valueOf(request.getParameter("storeId"));
            Integer toempId = Integer.valueOf(request.getParameter("empId"));
            StoreService storeService = new StoreService();
            Boolean ans = storeService.toempId(storeId, toempId);
            request.setAttribute("ans", ans);
            String url = "/Member/StoreServlet?action=reviewStore";
            RequestDispatcher successView = request.getRequestDispatcher(url);
            successView.forward(request, response);
        }
        //??????????????????
        if ("storestat".equals(action)) {
            storeId = Integer.valueOf(request.getParameter("storeId"));
            Integer status = Integer.valueOf(request.getParameter("status"));
            StoreService storeService = new StoreService();
            Boolean ans = storeService.tostat(storeId, status);
            request.setAttribute("ans", ans);
            //???????????????
            if (status == 0) {
                Store store0 = storeService.getById(storeId);
                store0.setStoreAcc(null);
                store0.setStorePwd(null);
                storeService.update(store0);
            }
            String url = "/Member/StoreServlet?action=reviewStore";
            RequestDispatcher successView = request.getRequestDispatcher(url);
            successView.forward(request, response);
        }
        //??????
        if ("StorePass".equals(action)) {
            StoreService storeService = new StoreService();
            JSONArray json = storeService.statStoreAll();
            request.setAttribute("list_stat", json);
            String url = "/back-end/store/reviewStorePass.jsp";
            RequestDispatcher successView = request.getRequestDispatcher(url);
            successView.forward(request, response);
        }
        //  chgpwd(update)
        if ("chgpwd".equals(action)) {
            List<String> errorMsgs = new LinkedList<String>();
            request.setAttribute("errorMsgs", errorMsgs);
            /*********************** 1.?????????????????? - ??????????????????????????? *************************/
            String storepwd = request.getParameter("STORE_PWD").trim();
            String storepwd2 = request.getParameter("STORE_PWD2").trim();
            String storepwd3 = request.getParameter("STORE_PWD3").trim();
            if (storepwd == null || storepwd.trim().length() == 0) {
                errorMsgs.add("????????????????????????");
            }
            if (storepwd2 == null || storepwd2.trim().length() == 0) {
                errorMsgs.add("?????????????????????");
            }
            if (storepwd3 == null || storepwd3.trim().length() == 0) {
                errorMsgs.add("????????????????????????");
            }
            if (!storepwd2.equals(storepwd3)) {
                errorMsgs.add("??????????????????????????????");
            }
            Store Store = new Store();
            Store.setStorePwd(storepwd2);
            if (!errorMsgs.isEmpty()) {
                request.setAttribute("Store", Store);
                RequestDispatcher failureView = request
                        .getRequestDispatcher("/front-end/store/Login/changepwd2.jsp");
                failureView.forward(request, response);
                return;
            }
            /*************************** 2.?????????????????? ***************************************/
            StoreService strSvc = new StoreService();
            Store store1 = strSvc.signin(strSvc.getById(storeId).getStoreAcc(), storepwd);
            Integer storeid2 = store1.getStoreId();
            if (storeid2 == 0) {
                errorMsgs.add("??????????????????");
                RequestDispatcher failureView = request.getRequestDispatcher("/front-end/store/Login/changepwd2.jsp");
                failureView.forward(request, response);
                return;// ????????????
            } else {
                Store = strSvc.changepwd(storeId, storepwd2);
            }
            /*************************** 3.????????????,????????????(Send the Success view) ***********/
            String url = "/front-end/store/Login/changepwd2.jsp";
            RequestDispatcher successView = request.getRequestDispatcher(url);
            successView.forward(request, response);
        }
        //update2(store info)
        if ("update2".equals(action)) {
            Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
            request.setAttribute("errorMsgs", errorMsgs);
            String storeaddress = request.getParameter("STORE_ADDRESS");
            if (storeaddress == null || storeaddress.trim().length() == 0) {
                errorMsgs.put("STORE_ADDRESS", "*????????????????????????");
            }
            String storeacc = request.getParameter("STORE_ACC");
            if (storeacc == null || storeacc.trim().length() == 0) {
                errorMsgs.put("STORE_ACC", "??????????????????");
            }
            String storehours = request.getParameter("STORE_HOURS");
            if (storehours == null || storehours.trim().length() == 0) {
                errorMsgs.put("STORE_HOURS", "??????????????????");
            }
            String storecomid = request.getParameter("STORE_COM_ID");
            if (!storecomid.trim().matches("^[0-9]{8}$")) {
                errorMsgs.put("STORE_COM_ID", "?????????????????????");
            }
            String storephone1 = request.getParameter("STORE_PHONE1");
            String storemail = request.getParameter("STORE_MAIL");
            if (!storemail.trim().matches("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z]+$")) {
                errorMsgs.put("STORE_MAIL", "email???????????????");
            }
            String storetext = request.getParameter("STORE_TEXT");
            String storeweb = request.getParameter("STORE_WEB");
            String storecomaddress = request.getParameter("STORE_COM_ADDRESS");
            if (storecomaddress == null || storecomaddress.trim().length() == 0) {
                errorMsgs.put("STORE_COM_ADDRESS", "??????????????????");
            }
            String storetwid = request.getParameter("STORE_TW_ID");
            if (storetwid == null || storetwid.trim().length() == 0) {
                errorMsgs.put("STORE_TW_ID", "?????????????????????");
            } else if (!storetwid.trim().matches("^[a-zA-Z]\\d{9}$")) {
                errorMsgs.put("STORE_TW_ID", "????????????????????????");
            }
            String storephone2 = request.getParameter("STORE_PHONE2");
            if (!errorMsgs.isEmpty()) {
                RequestDispatcher failureView = request
                        .getRequestDispatcher("/front-end/store/Login/storeRegister2.jsp");
                failureView.forward(request, response);
                return;
            }
            Integer storeid = (Integer) request.getSession().getAttribute("storeId");
            StoreService strsrv = new StoreService();
            strsrv.update(storeid, storeaddress, storeacc, storehours, storecomid, storephone1, storemail, storetext,
                    storeweb, storecomaddress, storephone2, storetwid);
            String url = "/Member/StoreServlet?action=searchinfo";
            RequestDispatcher successView = request.getRequestDispatcher(url);
            successView.forward(request, response);
        }
        //   searchinfo(store info) ------------------------------------------------------------------------------------------------------------
        if ("searchinfo".equals(action)) {
            StoreService strsvc = new StoreService();
            Store Store=strsvc.getById(storeId);
            request.setAttribute("Store", Store);
            String url = "/front-end/store/Login/storeRegister2.jsp";
            RequestDispatcher successView = request.getRequestDispatcher(url); // ?????????????????????listAllEmp.jsp
            successView.forward(request, response);

        }
    }
}