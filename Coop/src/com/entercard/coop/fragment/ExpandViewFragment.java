//package com.entercard.coop.fragment;
//
//import it.gmariotti.cardslib.library.internal.Card;
//import it.gmariotti.cardslib.library.internal.CardExpand;
//import it.gmariotti.cardslib.library.internal.CardHeader;
//import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;
//import it.gmariotti.cardslib.library.view.CardView;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.entercard.coop.ui.R;
//
//public class ExpandViewFragment extends Fragment {
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View rootView = inflater.inflate(R.layout.fragment_expand_listview, container, false);
//
//		initCards(rootView);
//		return rootView;
//	}
//
//	private void initCards(View rootView) {
//
//        //Create a Card
//        Card card = new Card(getActivity());
//
//        //Create a CardHeader
//        CardHeader header = new CardHeader(getActivity());
//
//        //Set the header title
//        header.setTitle("Click header");
//        //Add Header to card
//        card.addCardHeader(header);
//
//        //This provides a simple (and useless) expand area
//        CardExpandInside expand = new CardExpandInside(getActivity());
//        card.addCardExpand(expand);
//
//        //Set card in the cardView
//        CardView cardView = (CardView) rootView.findViewById(R.id.cardExpandableListView);
//        ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder()
//                        .highlightView(false)
//                        .setupView(cardView);
//        card.setViewToClickToExpand(viewToClickToExpand);
//
//        card.setOnExpandAnimatorEndListener(new Card.OnExpandAnimatorEndListener() {
//            @Override
//            public void onExpandEnd(Card card) {
//            	//TODO
//            }
//        });
//        cardView.setCard(card);
//    }
//	
//	/**
//	 * For SupportMap Fragment
//	 *
//	 */
//
//    class CardExpandInside extends CardExpand {
//
//        public CardExpandInside(Context context) {
//            super(context,R.layout.fragment_map_listvew);
//        }
//
//        @Override
//        public void setupInnerViewElements(ViewGroup parent, View view) {
//
//        }
//    }
//}
