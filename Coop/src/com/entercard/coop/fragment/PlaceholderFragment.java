//package com.entercard.coop.fragment;
//public static class PlaceholderFragment extends Fragment {
//		public static PlaceholderFragment newInstance(int sectionNumber) {
//			PlaceholderFragment fragment = new PlaceholderFragment();
//			return fragment;
//		}
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//
//			View rootView = inflater.inflate(R.layout.view_listvew, container,
//					false);
//
//			ArrayList<Card> cards = new ArrayList<Card>();
//			for (int i = 0; i < 50; i++) {
//				CardExample cardExample = new CardExample(getActivity(), "My title "
//						+ i, "Inner text " + i);
//				cards.add(cardExample);
//			}
//			CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(
//					getActivity(), cards);
//
//			CardListView listView = (CardListView) rootView
//					.findViewById(R.id.cardListView);
//			listView.setAdapter(mCardArrayAdapter);
//			return rootView;
//		}
//
//		public class CardExample extends Card {
//
//			protected String mTitleHeader;
//			protected String mTitleMain;
//
//			public CardExample(Context context, String titleHeader,
//					String titleMain) {
//				super(context, R.layout.row_card_list);
//				this.mTitleHeader = titleHeader;
//				this.mTitleMain = titleMain;
//				init();
//			}
//
//			private void init() {
//
//				// Create a CardHeader
//				CardHeader header = new CardHeader(getActivity());
//
//				// Set the header title
//				header.setTitle(mTitleHeader);
//
//				// // Add a popup menu. This method set OverFlow button to
//				// visible
//				// header.setPopupMenu(R.menu.context_popup,
//				// new CardHeader.OnClickCardHeaderPopupMenuListener() {
//				// @Override
//				// public void onMenuItemClick(BaseCard card,
//				// MenuItem item) {
//				// Toast.makeText(
//				// getActivity(),
//				// "Click on card menu" + mTitleHeader
//				// + " item=" + item.getTitle(),
//				// Toast.LENGTH_SHORT).show();
//				// }
//				// });
//				addCardHeader(header);
//
//				// Add ClickListener
//				setOnClickListener(new OnCardClickListener() {
//					@Override
//					public void onClick(Card card, View view) {
//						Toast.makeText(getContext(),
//								"Click Listener card=" + mTitleHeader,
//								Toast.LENGTH_SHORT).show();
//					}
//				});
//				// Set the card inner text
//				setTitle(mTitleMain);
//			}
//		}
//	}