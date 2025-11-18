function downloadFile(prodNo) {
   console.log("prodNo : " + prodNo);
   location.href =`/product/file/${prodNo}/download`;
}