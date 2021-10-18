package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.*;
import org.hibernate.sql.Update;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 회원 조회 시 엔티티를 외부에 직접 노출하지말고 무조건 DTO로 바꿔라!!
    // API를 만들때는 파라미터로 받는 나가든 엔티티를 절대 노출하거나 받지마! API 스펙에 맞는 DTO를 만들어서 사용해라.
    @GetMapping("api/v2/members")
    public Result<List<MemberDTO>> membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDTO> members = new ArrayList<>();

        for (Member findMember : findMembers) {
            members.add(new MemberDTO(findMember.getName(), findMember.getAddress()));
        }

        return new Result<>(members.size(), members);
    }


    // 회원 조회
    @GetMapping("api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    // 회원 수정 API
    // V1이 없음...
    @PutMapping("api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName()); // 수정
        Member findMember = memberService.findOne(id); // 수정된 회원 조회
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    /* V2(DTO)
       - 엔티티가 변해도 API 스펙이 바뀌지 않음
       - 엔티티와 프레젠테이션 계층을 위한 로직을 구분할 수 있음
       - DTO 객체만 보면 어떤 값이 쓰이는지 바로 구분이됨 (엔티티를 그대로 쓰면 어느 값이든 다 들어올수 있음)
       - API 요청과 응답은 반드시 DTO를 사용해서 한다. 엔티티쓰면 걍 호구샛기임
     */
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());
        member.setAddress(request.getAddress());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("api/v1/members")
    // @RequestBody: 클라이언트가 전송하는 Json 데이터를 Java Object로 변환해서 받아주는 역할
    // @ResponseBody: 자바 객체를 JSON 형태로 반환, 데이터를 바로 JSON or XML로 보내자!
    // @Valid: Controller 단에서 객체를 검증하고 싶을 때 사용!
    /* v1의 문제점...
       파라미터로 엔티티를 그대로 받기 때문에 엔티티의 내용이 수정되면 api의 스펙 자체가 변경되버림
       프레젠테이션 계층을 위한 로직이 존재하고 api 검증 로직이 엔티티 내에 존재함
       즉, 엔티티와 api 스펙이 1:1로 매핑되어 있는 상태! => 요청 스펙에 맞는 DTO 객체를 만들어서 사용
       실무에서는 등록 api가 여러개 존재하기 때문에 엔티티와 1:1 매핑을 하면 걍 GG 치고 나가야함(소셜 로그인, 일반 로그인 등등...)
       결론 => API 만들 때는 엔티티를 파라미터로 받지마라 ㅅㄱ
     */
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data // @Getter, @Setter, @EqualsAndHashCode, @ToString, @RequiredArgsConstructor 한번에~~~
    private static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    private static class CreateMemberRequest {

        // 검증 로직도 엔티티가 아닌 DTO에서 ㄱㄱ
        // 어떤 기능에선 name이 필요하지 않을수도 있다..
        @NotEmpty
        private String name;
        private Address address;
    }

    @Data
    @AllArgsConstructor
    private static class UpdateMemberResponse {

        private Long id;
        private String name;
    }

    @Data
    private static class UpdateMemberRequest {

        private String name;
    }

    @Data
    @AllArgsConstructor
    private static class Result<T> {

        private int count;
        private T members;
    }

    @Data
    @AllArgsConstructor
    private static class MemberDTO {

        // 노출하고 싶은 필드만 지정해서 사용
        // DTO - API가 1대1로 매핑되는 것
        private String name;
        private Address address;
    }
}
