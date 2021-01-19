<ul class="pagination">
	[#if hasPrevious]
		<li>
			<a href="javascript:;" data-page-number="${previousPageNumber}">&laquo;</a>
		</li>
	[#else]
		<li class="disabled">
			<a href="javascript:;">&laquo;</a>
		</li>
	[/#if]
	[#list segment as segmentPageNumber]
		<li[#if segmentPageNumber == pageNumber] class="active"[/#if]>
			<a href="javascript:;" data-page-number="${segmentPageNumber}">${segmentPageNumber}</a>
		</li>
	[/#list]
	[#if hasNext]
		<li>
			<a href="javascript:;" data-page-number="${nextPageNumber}">&raquo;</a>
		</li>
	[#else]
		<li class="disabled">
			<a href="javascript:;">&raquo;</a>
		</li>
	[/#if]
</ul>