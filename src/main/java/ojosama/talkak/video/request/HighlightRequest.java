package ojosama.talkak.video.request;

public record HighlightRequest(Integer index, String s3Url, String title, Long categoryId, Long memberId) {

}
