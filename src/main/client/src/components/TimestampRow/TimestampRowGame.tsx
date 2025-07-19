import React from 'react'
import { useParams, useNavigate } from 'react-router-dom';
import { TimestampRowView } from './TimestampRowView';


export default function TimestampRowGame() {
	
	const { mainId, currentPage } = useParams<{ mainId: string, currentPage: string }>();
	const navigate = useNavigate();


	const onNext = (mainId: string, currentPage: string ) => {
		
		  navigate(`/timestamprow/${mainId}/game/${currentPage}`);
		
		
	  };


  return (
	<div>
		{ mainId && currentPage ?
		<TimestampRowView currentPage={currentPage ? Number(currentPage) : 0} mainId={mainId} size={1} onNext={onNext} />
	:""}
	</div>
  )
}
